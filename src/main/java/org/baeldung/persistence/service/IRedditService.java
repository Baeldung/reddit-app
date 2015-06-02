package org.baeldung.persistence.service;

import java.util.List;

import org.baeldung.reddit.util.PostDto;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.fasterxml.jackson.databind.JsonNode;

public interface IRedditService {
    void loadAuthentication(final String name, final OAuth2AccessToken token);

    JsonNode SearchSubredditNames(String query);

    List<String> submitPost(PostDto postDto);
}
