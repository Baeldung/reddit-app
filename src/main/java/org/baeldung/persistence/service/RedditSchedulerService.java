package org.baeldung.persistence.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.baeldung.persistence.dao.PostRepository;
import org.baeldung.persistence.model.Post;
import org.baeldung.persistence.model.User;
import org.baeldung.reddit.util.RedditApiConstants;
import org.baeldung.reddit.util.UserAgentInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class RedditSchedulerService {

    @Autowired
    @Qualifier("schedulerRedditTemplate")
    private OAuth2RestTemplate redditRestTemplate;

    @Autowired
    private PostRepository postReopsitory;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public void submitPost(final Post post) {
        try {
            submitPostInternal(post);
        } catch (final Exception e) {
            logger.error("Error occurred while submitting post " + post.toString(), e);
        }
    }

    public int[] getPostScore(Post post) {
        final RestTemplate restTemplate = new RestTemplate();
        final List<ClientHttpRequestInterceptor> list = new ArrayList<ClientHttpRequestInterceptor>();
        list.add(new UserAgentInterceptor());
        restTemplate.setInterceptors(list);
        JsonNode node = restTemplate.getForObject("http://www.reddit.com/r/" + post.getSubreddit() + "/comments/" + post.getRedditID() + ".json", JsonNode.class);
        logger.info(node.toString());
        final int[] postInfo = new int[3];
        node = node.get(0).get("data").get("children").get(0).get("data");
        postInfo[0] = node.get("score").asInt();
        final double ratio = node.get("upvote_ratio").asDouble();
        postInfo[1] = (int) (ratio * 100);
        postInfo[2] = node.get("num_comments").asInt();
        return postInfo;
    }

    public void deletePost(String redditId) {
        logger.info("deleting post with id = " + redditId);
        final MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
        param.add("id", "t3_" + redditId);
        final JsonNode node = redditRestTemplate.postForObject("https://oauth.reddit.com/api/del.json", param, JsonNode.class);
        logger.info(node.toString());
    }

    public void checkAndReSubmit(Post post) {
        try {
            checkAndReSubmitInternal(post);
        } catch (final Exception e) {
            logger.error("Error occurred while check post " + post.toString(), e);
        }
    }

    public void checkAndDelete(Post post) {
        try {
            checkAndDeleteInternal(post);
        } catch (final Exception e) {
            logger.error("Error occurred while check post " + post.toString(), e);
        }
    }

    // === private methods

    private void submitPostInternal(final Post post) {
        final User user = post.getUser();
        final DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(user.getAccessToken());
        token.setRefreshToken(new DefaultOAuth2RefreshToken((user.getRefreshToken())));
        token.setExpiration(user.getTokenExpiration());
        redditRestTemplate.getOAuth2ClientContext().setAccessToken(token);
        //
        final UsernamePasswordAuthenticationToken userAuthToken = new UsernamePasswordAuthenticationToken(user.getUsername(), token.getValue(), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(userAuthToken);
        //
        final MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
        param.add(RedditApiConstants.TITLE, post.getTitle());
        param.add(RedditApiConstants.SR, post.getSubreddit());
        param.add(RedditApiConstants.URL, post.getUrl());
        param.add(RedditApiConstants.API_TYPE, "json");
        param.add(RedditApiConstants.KIND, "link");
        param.add(RedditApiConstants.RESUBMIT, "true");
        param.add(RedditApiConstants.THEN, "comments");
        if (post.isSendReplies()) {
            param.add(RedditApiConstants.SENDREPLIES, "true");
        }

        logger.info("Submit link with these parameters: " + param.entrySet());
        final JsonNode node = redditRestTemplate.postForObject("https://oauth.reddit.com/api/submit", param, JsonNode.class);
        parseResponse(node, post);
    }

    private void parseResponse(JsonNode node, Post post) {
        final JsonNode errorNode = node.get("json").get("errors").get(0);
        if (errorNode == null) {
            post.setSent(true);
            post.setSubmissionResponse("Successfully sent");
            post.setRedditID(node.get("json").get("data").get("id").asText());
            post.setNoOfAttempts(post.getNoOfAttempts() - 1);
            postReopsitory.save(post);
            logger.info("Successfully sent " + post.toString());
        } else {
            post.setSubmissionResponse(errorNode.toString());
            postReopsitory.save(post);
            logger.info("Error occurred: " + errorNode.toString() + "while submitting post " + post.toString());
        }
    }

    private void checkAndReSubmitInternal(Post post) {
        logger.info(post.toString());
        if (didIntervalPassed(post.getSubmissionDate(), post.getTimeInterval())) {
            final int[] postInfo = getPostScore(post);
            final boolean keepBecauseComment = (postInfo[2] > 0) && post.isKeepIfHasComments();
            if (((postInfo[0] < post.getMinScoreRequired()) || (postInfo[1] < post.getMinUpvoteRatio())) && !keepBecauseComment) {
                deletePost(post.getRedditID());
                resetPost(post);
            } else {
                post.setNoOfAttempts(0);
                post.setRedditID(null);
                postReopsitory.save(post);
            }
        }
    }

    private void checkAndDeleteInternal(Post post) {
        logger.info(post.toString());
        if (didIntervalPassed(post.getSubmissionDate(), post.getTimeInterval())) {
            final int[] postInfo = getPostScore(post);
            if (((postInfo[0] < post.getMinScoreRequired()) || (postInfo[1] < post.getMinUpvoteRatio())) && !((postInfo[2] > 0) && post.isKeepIfHasComments())) {
                deletePost(post.getRedditID());
                post.setSubmissionResponse("Consumed Attempts without reaching score");
                post.setRedditID(null);
                postReopsitory.save(post);
            } else {
                post.setNoOfAttempts(0);
                post.setRedditID(null);
                postReopsitory.save(post);
            }
        }
    }

    private boolean didIntervalPassed(Date submissonDate, int postInterval) {
        final long currentTime = new Date().getTime();
        final long interval = currentTime - submissonDate.getTime();
        final long intervalInMinutes = TimeUnit.MINUTES.convert(interval, TimeUnit.MILLISECONDS);
        return intervalInMinutes > postInterval;
    }

    private void resetPost(Post post) {
        long time = new Date().getTime();
        time += TimeUnit.MILLISECONDS.convert(post.getTimeInterval(), TimeUnit.MINUTES);
        post.setRedditID(null);
        post.setSubmissionDate(new Date(time));
        post.setSent(false);
        post.setSubmissionResponse("Not sent yet");
        postReopsitory.save(post);
    }

}
