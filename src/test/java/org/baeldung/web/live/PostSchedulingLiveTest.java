package org.baeldung.web.live;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import org.baeldung.persistence.dao.PostRepository;
import org.baeldung.persistence.dao.PreferenceRepository;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.Post;
import org.baeldung.persistence.model.Preference;
import org.baeldung.persistence.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.response.Response;

public class PostSchedulingLiveTest extends AbstractLiveTest {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PreferenceRepository preferenceReopsitory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User userJohn;

    private List<Post> posts;

    @Before
    public void init() throws ParseException {
        userJohn = userRepository.findByUsername("john");
        if (userJohn == null) {
            userJohn = new User();
            userJohn.setUsername("john");
            userJohn.setPassword(passwordEncoder.encode("123"));
            userJohn.setAccessToken("token");
            userRepository.save(userJohn);
        }
        Preference pref = userJohn.getPreference();
        if (pref == null) {
            pref = new Preference();
            userJohn.setPreference(pref);
        }
        pref.setTimezone(TimeZone.getDefault().getID());
        preferenceReopsitory.save(pref);
        userRepository.save(userJohn);
        posts = postRepository.findByUser(userJohn);
        if (posts.size() == 0) {
            final Post post = new Post();
            post.setTitle("test");
            post.setUrl("test.com");
            post.setSubreddit("test");
            post.setSubmissionDate(dateFormat.parse("2016-11-11 00:00"));
            post.setUser(userJohn);
            postRepository.save(post);
        }

    }

    @Test
    public void whenGettingUserScheduledPosts_thenCorrect() {
        final Response response = givenAuth().get(URL_PREFIX + "/api/scheduledPosts?page=0");

        assertEquals(200, response.statusCode());
        assertTrue(response.as(List.class).size() > 0);
    }

    @Test
    public void whenSchedulingNewPost_thenCorrect() throws JsonProcessingException, ParseException {
        final Post post = new Post();
        post.setTitle("test2");
        post.setUrl("test2.com");
        post.setSubreddit("test2");
        post.setSubmissionDate(dateFormat.parse("2016-01-01 00:00"));
        final Response response = withRequestBody(givenAuth(), post).post(URL_PREFIX + "/api/scheduledPosts?date=2016-01-01 00:00");

        assertEquals(200, response.statusCode());
        assertTrue(postRepository.findByUser(userJohn).size() > posts.size());
    }

    @Test
    public void whenUpdatingSheduledPost_thenUpdated() throws JsonProcessingException {
        final Post post = posts.get(0);
        post.setTitle("new title");
        Response response = withRequestBody(givenAuth(), post).put(URL_PREFIX + "/api/scheduledPosts/" + post.getId() + "?date=2016-01-01 00:00");

        assertEquals(200, response.statusCode());
        response = givenAuth().get(URL_PREFIX + "/api/scheduledPosts/" + post.getId());
        assertTrue(response.asString().contains(post.getTitle()));
    }

    @Test
    public void whenDeletingScheduledPost_thenDeleted() {
        final Post post = posts.get(0);
        final Response response = givenAuth().delete(URL_PREFIX + "/api/scheduledPosts/" + post.getId());

        assertEquals(204, response.statusCode());
        assertNull(postRepository.findOne(post.getId()));
    }

}
