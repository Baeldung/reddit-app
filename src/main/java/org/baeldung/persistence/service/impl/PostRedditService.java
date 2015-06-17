package org.baeldung.persistence.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.baeldung.persistence.dao.PostRepository;
import org.baeldung.persistence.model.Post;
import org.baeldung.persistence.model.User;
import org.baeldung.persistence.service.IPostRedditService;
import org.baeldung.reddit.util.OnPostSubmittedEvent;
import org.baeldung.reddit.util.PostScores;
import org.baeldung.reddit.util.RedditApiConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
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
class PostRedditService implements IPostRedditService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("schedulerRedditTemplate")
    private OAuth2RestTemplate redditRestTemplate;

    @Autowired
    @Qualifier("simpleRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private PostRepository postReopsitory;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    // API

    @Override
    public void submitPost(final Post post) {
        try {
            submitPostInternal(post);
        } catch (final Exception e) {
            logger.error("Error occurred while submitting post " + post.toString(), e);
        }
    }

    @Override
    public PostScores getPostScores(final Post post) {
        JsonNode node = restTemplate.getForObject("http://www.reddit.com/r/" + post.getSubreddit() + "/comments/" + post.getRedditID() + ".json", JsonNode.class);

        final PostScores postScore = new PostScores();
        node = node.get(0).get("data").get("children").get(0).get("data");
        postScore.setScore(node.get("score").asInt());

        final double ratio = node.get("upvote_ratio").asDouble();
        postScore.setUpvoteRatio((int) (ratio * 100));
        postScore.setNoOfComments(node.get("num_comments").asInt());

        return postScore;
    }

    @Override
    public void deletePost(final String redditId) {
        logger.info("Deleting post with id = {}", redditId);

        final MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
        param.add("id", "t3_" + redditId);
        final JsonNode node = redditRestTemplate.postForObject("https://oauth.reddit.com/api/del.json", param, JsonNode.class);

        logger.info("Deleting post response = {}", node.toString());
    }

    @Override
    public void checkAndReSubmit(final Post post) {
        try {
            checkAndReSubmitInternal(post);
        } catch (final Exception e) {
            logger.error("Error occurred while checking and resubmitting post = " + post.toString(), e);
        }
    }

    @Override
    public void checkAndDelete(final Post post) {
        try {
            checkAndDeleteInternal(post);
        } catch (final Exception e) {
            logger.error("Error occurred while checking and deleting post " + post.toString(), e);
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

        logger.info("Submitting link with these parameters = {}", param.entrySet());
        final JsonNode node = redditRestTemplate.postForObject("https://oauth.reddit.com/api/submit", param, JsonNode.class);
        updatePostFromResponse(node, post);
    }

    private void updatePostFromResponse(final JsonNode node, final Post post) {
        final JsonNode errorNode = node.get("json").get("errors").get(0);
        if (errorNode == null) {
            post.setSent(true);
            post.setSubmissionResponse("Successfully sent");
            post.setRedditID(node.get("json").get("data").get("id").asText());
            post.setNoOfAttempts(post.getNoOfAttempts() - 1);
            postReopsitory.save(post);

            logger.info("Successfully sent post = " + post.toString());
            final String email = post.getUser().getPreference().getEmail();
            if (email != null) {
                logger.info("Sending notification email to " + email);
                eventPublisher.publishEvent(new OnPostSubmittedEvent(post, email));
            }
        } else {
            post.setSubmissionResponse(errorNode.toString());
            postReopsitory.save(post);
            logger.info("Error occurred: " + errorNode.toString() + "while submitting post " + post.toString());
        }
    }

    private void checkAndReSubmitInternal(final Post post) {
        logger.info("Checking and Resubmitting post = {}", post.toString());
        if (didIntervalPass(post.getSubmissionDate(), post.getTimeInterval())) {
            if (didPostGoalFail(post)) {
                deletePost(post.getRedditID());
                resetPost(post);
            } else {
                post.setNoOfAttempts(0);
                post.setRedditID(null);
                postReopsitory.save(post);
            }
        }
    }

    private void checkAndDeleteInternal(final Post post) {
        logger.info("Checking and deleting post = {}", post.toString());
        if (didIntervalPass(post.getSubmissionDate(), post.getTimeInterval())) {
            if (didPostGoalFail(post)) {
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

    private boolean didIntervalPass(final Date submissonDate, final int postInterval) {
        final long currentTime = new Date().getTime();
        final long interval = currentTime - submissonDate.getTime();
        final long intervalInMinutes = TimeUnit.MINUTES.convert(interval, TimeUnit.MILLISECONDS);
        return intervalInMinutes > postInterval;
    }

    private void resetPost(final Post post) {
        long time = new Date().getTime();
        time += TimeUnit.MILLISECONDS.convert(post.getTimeInterval(), TimeUnit.MINUTES);
        post.setRedditID(null);
        post.setSubmissionDate(new Date(time));
        post.setSent(false);
        post.setSubmissionResponse("Not sent yet");
        postReopsitory.save(post);
    }

    private boolean didPostGoalFail(final Post post) {
        final PostScores postScores = getPostScores(post);
        final int score = postScores.getScore();
        final int upvoteRatio = postScores.getUpvoteRatio();
        final int noOfComments = postScores.getNoOfComments();
        return (((score < post.getMinScoreRequired()) || (upvoteRatio < post.getMinUpvoteRatio())) && !((noOfComments > 0) && post.isKeepIfHasComments()));
    }

}
