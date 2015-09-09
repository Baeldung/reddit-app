package org.baeldung.web.live;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.ParseException;

import org.baeldung.web.dto.command.ScheduledPostAddCommandDto;
import org.baeldung.web.dto.query.ScheduledPostQueryDto;
import org.junit.Test;

import com.jayway.restassured.response.Response;

public class ResubmitOptionsLiveTest extends AbstractBaseLiveTest {
    private static final String DATE = "2016-01-01 00:00";

    // tests

    @Test
    public void givenResubmitOptionsDeactivated_whenScheduleANewPost_thenCreated() throws ParseException, IOException {
        final ScheduledPostAddCommandDto post = createPost();
        post.setResubmitOptionsActivated(false);
        final Response response = withRequestBody(givenAuth(), post).post(urlPrefix + "/api/scheduledPosts");

        assertEquals(201, response.statusCode());
        final ScheduledPostQueryDto result = objectMapper.reader().forType(ScheduledPostQueryDto.class).readValue(response.asString());
        assertEquals(result.getUrl(), post.getUrl());
    }

    @Test
    public void givenResubmitOptionsActivated_whenScheduleANewPostWithZeroAttempts_thenInvalid() throws ParseException, IOException {
        final ScheduledPostAddCommandDto post = createPost();
        post.setNoOfAttempts(0);
        post.setMinScoreRequired(5);
        post.setTimeInterval(60);
        post.setResubmitOptionsActivated(true);

        final Response response = withRequestBody(givenAuth(), post).post(urlPrefix + "/api/scheduledPosts");

        assertEquals(400, response.statusCode());
        assertTrue(response.asString().contains("Invalid Resubmit Options"));
    }

    @Test
    public void givenResubmitOptionsActivated_whenScheduleANewPostWithZeroMinScore_thenInvalid() throws ParseException, IOException {
        final ScheduledPostAddCommandDto post = createPost();
        post.setMinScoreRequired(0);
        post.setNoOfAttempts(3);
        post.setTimeInterval(60);
        post.setResubmitOptionsActivated(true);

        final Response response = withRequestBody(givenAuth(), post).post(urlPrefix + "/api/scheduledPosts");

        assertEquals(400, response.statusCode());
        assertTrue(response.asString().contains("Invalid Resubmit Options"));
    }

    @Test
    public void givenResubmitOptionsActivated_whenScheduleANewPostWithZeroTimeInterval_thenInvalid() throws ParseException, IOException {
        final ScheduledPostAddCommandDto post = createPost();
        post.setTimeInterval(0);
        post.setMinScoreRequired(5);
        post.setNoOfAttempts(3);
        post.setResubmitOptionsActivated(true);

        final Response response = withRequestBody(givenAuth(), post).post(urlPrefix + "/api/scheduledPosts");

        assertEquals(400, response.statusCode());
        assertTrue(response.asString().contains("Invalid Resubmit Options"));
    }

    @Test
    public void givenResubmitOptionsActivated_whenScheduleANewPostWithValidResubmitOptions_thenCreated() throws ParseException, IOException {
        final ScheduledPostAddCommandDto post = createPost();
        post.setMinScoreRequired(5);
        post.setNoOfAttempts(3);
        post.setTimeInterval(60);
        post.setResubmitOptionsActivated(true);

        final Response response = withRequestBody(givenAuth(), post).post(urlPrefix + "/api/scheduledPosts");

        assertEquals(201, response.statusCode());
        final ScheduledPostQueryDto result = objectMapper.reader().forType(ScheduledPostQueryDto.class).readValue(response.asString());
        assertEquals(result.getUrl(), post.getUrl());
    }

    //

    private ScheduledPostAddCommandDto createPost() throws ParseException {
        final ScheduledPostAddCommandDto post = new ScheduledPostAddCommandDto();
        post.setTitle(randomAlphabetic(6));
        post.setUrl("test.com");
        post.setSubreddit(randomAlphabetic(6));
        post.setDate(DATE);
        return post;
    }
}
