package org.baeldung.web.live;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.ParseException;

import org.baeldung.persistence.model.Post;
import org.junit.Test;

import com.jayway.restassured.response.Response;

public class ResubmitOptionsLiveTest extends AbstractLiveTest {
    private static final String date = "2016-01-01 00:00";

    @Test
    public void givenResubmitOptionsDeactivated_whenScheduleANewPost_thenCreated() throws ParseException, IOException {
        final Post post = createPost();

        final Response response = withRequestBody(givenAuth(), post).post(urlPrefix + "/api/scheduledPosts?date=" + date + "&resubmitOptionsActivated=false");

        assertEquals(201, response.statusCode());
        final Post result = objectMapper.reader().forType(Post.class).readValue(response.asString());
        assertEquals(result.getUrl(), post.getUrl());
    }

    @Test
    public void givenResubmitOptionsActivated_whenScheduleANewPostWithZeroAttempts_thenInvalid() throws ParseException, IOException {
        final Post post = createPost();
        post.setNoOfAttempts(0);
        post.setMinScoreRequired(5);
        post.setTimeInterval(60);

        final Response response = withRequestBody(givenAuth(), post).post(urlPrefix + "/api/scheduledPosts?date=" + date + "&resubmitOptionsActivated=true");

        assertEquals(400, response.statusCode());
        assertTrue(response.asString().contains("Invalid Resubmit Options"));
    }

    @Test
    public void givenResubmitOptionsActivated_whenScheduleANewPostWithZeroMinScore_thenInvalid() throws ParseException, IOException {
        final Post post = createPost();
        post.setMinScoreRequired(0);
        post.setNoOfAttempts(3);
        post.setTimeInterval(60);

        final Response response = withRequestBody(givenAuth(), post).post(urlPrefix + "/api/scheduledPosts?date=" + date + "&resubmitOptionsActivated=true");

        assertEquals(400, response.statusCode());
        assertTrue(response.asString().contains("Invalid Resubmit Options"));
    }

    @Test
    public void givenResubmitOptionsActivated_whenScheduleANewPostWithZeroTimeInterval_thenInvalid() throws ParseException, IOException {
        final Post post = createPost();
        post.setTimeInterval(0);
        post.setMinScoreRequired(5);
        post.setNoOfAttempts(3);

        final Response response = withRequestBody(givenAuth(), post).post(urlPrefix + "/api/scheduledPosts?date=" + date + "&resubmitOptionsActivated=true");

        assertEquals(400, response.statusCode());
        assertTrue(response.asString().contains("Invalid Resubmit Options"));
    }

    @Test
    public void givenResubmitOptionsActivated_whenScheduleANewPostWithValidResubmitOptions_thenCreated() throws ParseException, IOException {
        final Post post = createPost();
        post.setMinScoreRequired(5);
        post.setNoOfAttempts(3);
        post.setTimeInterval(60);

        final Response response = withRequestBody(givenAuth(), post).post(urlPrefix + "/api/scheduledPosts?date=" + date + "&resubmitOptionsActivated=true");

        assertEquals(201, response.statusCode());
        final Post result = objectMapper.reader().forType(Post.class).readValue(response.asString());
        assertEquals(result.getUrl(), post.getUrl());
    }

    //

    private Post createPost() throws ParseException {
        final Post post = new Post();
        post.setTitle(randomAlphabetic(6));
        post.setUrl("test.com");
        post.setSubreddit(randomAlphabetic(6));
        post.setSubmissionDate(dateFormat.parse(date));
        return post;
    }
}
