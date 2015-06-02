package org.baeldung.persistence.service;

import java.util.Arrays;
import java.util.List;

import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.User;
import org.baeldung.reddit.util.PostDto;
import org.baeldung.reddit.util.RedditApiConstants;
import org.baeldung.web.RedditTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.JsonNode;

@Service
class RedditService implements IRedditService {
    @Autowired
    private RedditTemplate redditTemplate;

    @Autowired
    private UserRepository userReopsitory;

    @Override
    public void loadAuthentication(final String name, final OAuth2AccessToken token) {
        User user = userReopsitory.findByUsername(name);
        if (user == null) {
            user = new User();
            user.setUsername(name);
        }

        if (redditTemplate.needsCaptcha().equalsIgnoreCase("true")) {
            user.setNeedCaptcha(true);
        } else {
            user.setNeedCaptcha(false);
        }

        user.setAccessToken(token.getValue());
        user.setRefreshToken(token.getRefreshToken().getValue());
        user.setTokenExpiration(token.getExpiration());
        userReopsitory.save(user);

        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, token.getValue(), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Override
    public JsonNode SearchSubredditNames(String query) {
        final MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
        param.add("query", query);
        final JsonNode node = redditTemplate.subredditNameSearch(query);
        return node.get("names");
    }

    @Override
    public List<String> submitPost(PostDto postDto) {
        final MultiValueMap<String, String> param1 = constructParams(postDto);
        final JsonNode node = redditTemplate.submitPost(param1);
        return parseResponse(node);
    }

    // === private

    private final MultiValueMap<String, String> constructParams(PostDto postDto) {
        final MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
        param.add(RedditApiConstants.TITLE, postDto.getTitle());
        param.add(RedditApiConstants.SR, postDto.getSubreddit());
        param.add(RedditApiConstants.URL, postDto.getUrl());
        param.add(RedditApiConstants.IDEN, postDto.getIden());
        param.add(RedditApiConstants.CAPTCHA, postDto.getCaptcha());
        param.add(RedditApiConstants.URL, postDto.getUrl());
        if (postDto.isSendreplies()) {
            param.add(RedditApiConstants.SENDREPLIES, "true");
        }

        param.add(RedditApiConstants.API_TYPE, "json");
        param.add(RedditApiConstants.KIND, "link");
        param.add(RedditApiConstants.RESUBMIT, "true");
        param.add(RedditApiConstants.THEN, "comments");
        return param;
    }

    private final List<String> parseResponse(final JsonNode node) {
        String result = "";
        final JsonNode errorNode = node.get("json").get("errors").get(0);
        if (errorNode != null) {
            for (final JsonNode child : errorNode) {
                result = result + child.toString().replaceAll("\"|null", "") + "<br>";
            }
            return Arrays.asList(result);
        } else {
            if ((node.get("json").get("data") != null) && (node.get("json").get("data").get("url") != null)) {
                return Arrays.asList("Post submitted successfully", node.get("json").get("data").get("url").asText());
            } else {
                return Arrays.asList("Error Occurred while parsing Response");
            }
        }
    }
}
