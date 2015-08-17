package org.baeldung.web.live;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.baeldung.web.ScheduledPostDto;
import org.junit.Test;

import com.jayway.restassured.response.Response;

public class ScheduledPostLiveTest extends AbstractLiveTest {
    private static final String date = "2016-01-01 00:00";

    @Test
    public void whenScheduleANewPost_thenCreated() throws ParseException, IOException {
        final ScheduledPostDto post = new ScheduledPostDto();
        post.setTitle(randomAlphabetic(6));
        post.setUrl("test.com");
        post.setSubreddit(randomAlphabetic(6));
        post.setDate(date);

        final Response response = withRequestBody(givenAuth(), post).queryParams("resubmitOptionsActivated", false).post(urlPrefix + "/api/scheduledPosts");

        assertEquals(201, response.statusCode());
        final ScheduledPostDto result = objectMapper.reader().forType(ScheduledPostDto.class).readValue(response.asString());
        assertEquals(result.getUrl(), post.getUrl());
    }

    @Test
    public void whenGettingUserScheduledPosts_thenCorrect() throws ParseException, IOException {
        createPost();

        final Response response = givenAuth().get(urlPrefix + "/api/scheduledPosts");

        assertEquals(200, response.statusCode());
        assertTrue(response.as(List.class).size() > 0);
    }

    @Test
    public void whenUpdatingScheduledPost_thenUpdated() throws ParseException, IOException {
        final ScheduledPostDto post = createPost();

        post.setTitle("new title");
        Response response = withRequestBody(givenAuth(), post).queryParams("resubmitOptionsActivated", false).put(urlPrefix + "/api/scheduledPosts/" + post.getId());

        assertEquals(200, response.statusCode());
        response = givenAuth().get(urlPrefix + "/api/scheduledPosts/" + post.getId());
        assertTrue(response.asString().contains(post.getTitle()));
    }

    @Test
    public void whenDeletingScheduledPost_thenDeleted() throws ParseException, IOException {
        final ScheduledPostDto post = createPost();
        final Response response = givenAuth().delete(urlPrefix + "/api/scheduledPosts/" + post.getId());

        assertEquals(204, response.statusCode());
    }

    //

    protected ScheduledPostDto createPost() throws ParseException, IOException {
        final ScheduledPostDto post = new ScheduledPostDto();
        post.setTitle(randomAlphabetic(6));
        post.setUrl("test.com");
        post.setSubreddit(randomAlphabetic(6));
        post.setDate(date);
        final Response response = withRequestBody(givenAuth(), post).queryParams("resubmitOptionsActivated", false).post(urlPrefix + "/api/scheduledPosts");
        return objectMapper.reader().forType(ScheduledPostDto.class).readValue(response.asString());
    }

}
