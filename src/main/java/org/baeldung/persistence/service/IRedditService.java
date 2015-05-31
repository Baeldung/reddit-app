package org.baeldung.persistence.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

public interface IRedditService {
    void loadAuthentication(final String name, final OAuth2AccessToken token);

    String SearchSubredditNames(String query);

    List<String> submitPost(Map<String, String> formParams);
}
