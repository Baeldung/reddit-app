package org.baeldung.web.live;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.baeldung.persistence.model.Post;
import org.junit.Ignore;
import org.junit.Test;

import com.jayway.restassured.response.Response;

@Ignore
public class ScheduledPostLiveTest extends AbstractLiveTest {
    private static final String date = "2016-01-01 00:00";

    @Test
    public void whenScheduleANewPost_thenCreated() throws ParseException, IOException {
        final Post post = new Post();
        post.setTitle("test");
        post.setUrl("test.com");
        post.setSubreddit("test");
        post.setSubmissionDate(dateFormat.parse(date));

        final Response response = withRequestBody(givenAuth(), post).post(urlPrefix + "/api/scheduledPosts?date=" + date);

        assertEquals(200, response.statusCode());
        final Post result = objectMapper.reader().forType(Post.class).readValue(response.asString());
        assertEquals(result.getUrl(), post.getUrl());
    }

    @Test
    public void whenGettingUserScheduledPosts_thenCorrect() throws ParseException, IOException {
        createPost();

        final Response response = givenAuth().get(urlPrefix + "/api/scheduledPosts?page=0");

        assertEquals(200, response.statusCode());
        assertTrue(response.as(List.class).size() > 0);
    }

    @Test
    public void whenUpdatingScheduledPost_thenUpdated() throws ParseException, IOException {
        final Post post = createPost();

        post.setTitle("new title");
        Response response = withRequestBody(givenAuth(), post).put(urlPrefix + "/api/scheduledPosts/" + post.getId() + "?date=" + date);

        assertEquals(200, response.statusCode());
        response = givenAuth().get(urlPrefix + "/api/scheduledPosts/" + post.getId());
        assertTrue(response.asString().contains(post.getTitle()));
    }

    @Test
    public void whenDeletingScheduledPost_thenDeleted() throws ParseException, IOException {
        final Post post = createPost();
        final Response response = givenAuth().delete(urlPrefix + "/api/scheduledPosts/" + post.getId());

        assertEquals(204, response.statusCode());
    }

    //

    private Post createPost() throws ParseException, IOException {
        final Post post = new Post();
        post.setTitle("test");
        post.setUrl("test.com");
        post.setSubreddit("test");
        post.setSubmissionDate(dateFormat.parse(date));
        final Response response = withRequestBody(givenAuth(), post).post(urlPrefix + "/api/scheduledPosts?date=" + date);
        return objectMapper.reader().forType(Post.class).readValue(response.asString());
    }

}
