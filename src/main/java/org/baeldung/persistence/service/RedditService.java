package org.baeldung.persistence.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.User;
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
public class RedditService implements IRedditService {
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
    public String SearchSubredditNames(String query) {
        final MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
        param.add("query", query);
        final JsonNode node = redditTemplate.subredditNameSearch(query);
        return node.get("names").toString();
    }

    @Override
    public List<String> submitPost(Map<String, String> formParams) {
        final MultiValueMap<String, String> param1 = constructParams(formParams);
        final JsonNode node = redditTemplate.submitPost(param1);
        return parseResponse(node);
    }

    // === private

    private final MultiValueMap<String, String> constructParams(final Map<String, String> formParams) {
        final MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
        param.add(RedditApiConstants.API_TYPE, "json");
        param.add(RedditApiConstants.KIND, "link");
        param.add(RedditApiConstants.RESUBMIT, "true");
        param.add(RedditApiConstants.THEN, "comments");
        for (final Map.Entry<String, String> entry : formParams.entrySet()) {
            param.add(entry.getKey(), entry.getValue());
        }
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
