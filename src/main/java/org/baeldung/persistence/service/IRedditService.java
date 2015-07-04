package org.baeldung.persistence.service;

import java.util.List;

import org.baeldung.reddit.util.PostDto;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public interface IRedditService {

    void loadAuthentication(final String name, final OAuth2AccessToken token);

    List<String> submitPost(final PostDto postDto);

    List<String> searchSubreddit(final String query);

    void connectReddit(final boolean needsCaptcha, final OAuth2AccessToken token);

}
