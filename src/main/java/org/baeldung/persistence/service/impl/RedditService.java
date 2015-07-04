package org.baeldung.persistence.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.baeldung.persistence.dao.PreferenceRepository;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.Preference;
import org.baeldung.persistence.model.User;
import org.baeldung.persistence.service.IRedditService;
import org.baeldung.reddit.util.PostDto;
import org.baeldung.reddit.util.RedditApiConstants;
import org.baeldung.web.RedditTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedditTemplate redditTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PreferenceRepository preferenceRepository;

    private List<String> subreddits;

    public RedditService() {
        loadSubreddits();
    }

    // API

    @Override
    public void loadAuthentication(final String name, final OAuth2AccessToken token) {
        User user = userRepository.findByUsername(name);
        if (user == null) {
            user = new User();
            user.setUsername(name);
        }

        if (redditTemplate.needsCaptcha()) {
            user.setNeedCaptcha(true);
        } else {
            user.setNeedCaptcha(false);
        }

        user.setAccessToken(token.getValue());
        user.setRefreshToken(token.getRefreshToken().getValue());
        user.setTokenExpiration(token.getExpiration());

        final Preference pref = new Preference();
        pref.setTimezone(TimeZone.getDefault().getID());
        preferenceRepository.save(pref);
        user.setPreference(pref);
        userRepository.save(user);

        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, token.getValue(), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Override
    public void connectReddit(final boolean needsCaptcha, final OAuth2AccessToken token) {
        final User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        currentUser.setNeedCaptcha(needsCaptcha);
        currentUser.setAccessToken(token.getValue());
        currentUser.setRefreshToken(token.getRefreshToken().getValue());
        currentUser.setTokenExpiration(token.getExpiration());
        userRepository.save(currentUser);
    }

    @Override
    public List<String> submitPost(final PostDto postDto) {
        final MultiValueMap<String, String> param1 = constructParams(postDto);
        final JsonNode node = redditTemplate.submitPost(param1);
        return parseResponse(node);
    }

    @Override
    public List<String> searchSubreddit(final String query) {
        return subreddits.stream().filter(sr -> sr.startsWith(query)).limit(9).collect(Collectors.toList());
    }

    // === private

    private final MultiValueMap<String, String> constructParams(final PostDto postDto) {
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

    private void loadSubreddits() {
        subreddits = new ArrayList<String>();
        try {
            final Resource resource = new ClassPathResource("subreddits.csv");
            final Scanner scanner = new Scanner(resource.getFile());
            scanner.useDelimiter(",");
            while (scanner.hasNext()) {
                subreddits.add(scanner.next());
            }
            scanner.close();
        } catch (final IOException e) {
            logger.error("error while loading subreddits", e);
        }

    }

    // old autocomplete subreddit
    // private JsonNode searchSubredditNames(String query) {
    // final MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
    // param.add("query", query);
    // final JsonNode node = redditTemplate.subredditNameSearch(query);
    // return node.get("names");
    // }
}
