package org.baeldung.web.live;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.baeldung.web.dto.command.FeedAddCommandDto;
import org.baeldung.web.dto.query.FeedQueryDto;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.jayway.restassured.response.Response;

public class MyFeedLiveTest extends AbstractLiveTest<FeedQueryDto> {

    // test

    @Test
    public void whenGettingUserSites_thenCorrect() throws ParseException, IOException {
        newDto();
        final Response response = givenAuth().get(urlPrefix + "/api/myFeeds");

        assertEquals(200, response.statusCode());
        assertTrue(response.as(List.class).size() > 0);
    }

    @Test
    public void whenGettingSiteArticles_thenCorrect() throws ParseException, IOException {
        final FeedQueryDto feed = newDto();
        final Response response = givenAuth().get(urlPrefix + "/api/myFeeds/articles?id=" + feed.getId());

        assertEquals(200, response.statusCode());
        assertTrue(response.as(List.class).size() > 0);
    }

    @Test
    public void whenAddingNewSite_thenCorrect() throws ParseException, IOException {
        final FeedQueryDto feed = newDto();

        final Response response = givenAuth().get(urlPrefix + "/api/myFeeds");

        assertTrue(response.asString().contains(feed.getUrl()));
    }

    @Test
    public void whenDeletingSite_thenDeleted() throws ParseException, IOException {
        final FeedQueryDto feed = newDto();
        final Response response = givenAuth().delete(urlPrefix + "/api/myFeeds/" + feed.getId());

        assertEquals(204, response.statusCode());
    }

    //

    @Override
    protected FeedQueryDto newDto() throws ParseException, IOException {
        final FeedAddCommandDto feed = new FeedAddCommandDto("baeldung", "http://www.baeldung.com/feed/");

        final Response response = withRequestBody(givenAuth(), feed).post(urlPrefix + "/api/myFeeds");

        return objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).reader().forType(FeedQueryDto.class).readValue(response.asString());
    }

}
