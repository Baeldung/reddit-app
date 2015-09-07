package org.baeldung.web.live;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.baeldung.web.FeedDto;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.jayway.restassured.response.Response;

public class MyFeedLiveTest extends AbstractLiveTest<FeedDto> {

    // test

    @Test
    public void whenGettingUserSites_thenCorrect() throws ParseException, IOException {
        newDto();
        final Response response = givenAuth().get(urlPrefix + "/myFeeds");

        assertEquals(200, response.statusCode());
        assertTrue(response.as(List.class).size() > 0);
    }

    @Test
    public void whenGettingSiteArticles_thenCorrect() throws ParseException, IOException {
        final FeedDto feed = newDto();
        final Response response = givenAuth().get(urlPrefix + "/myFeeds/articles?id=" + feed.getId());

        assertEquals(200, response.statusCode());
        assertTrue(response.as(List.class).size() > 0);
    }

    @Test
    public void whenAddingNewSite_thenCorrect() throws ParseException, IOException {
        final FeedDto feed = newDto();

        final Response response = givenAuth().get(urlPrefix + "/myFeeds");

        assertTrue(response.asString().contains(feed.getUrl()));
    }

    @Test
    public void whenDeletingSite_thenDeleted() throws ParseException, IOException {
        final FeedDto feed = newDto();
        final Response response = givenAuth().delete(urlPrefix + "/myFeeds/" + feed.getId());

        assertEquals(204, response.statusCode());
    }

    //

    @Override
    protected FeedDto newDto() throws ParseException, IOException {
        final FeedDto feed = new FeedDto();
        feed.setUrl("http://www.baeldung.com/feed/");
        feed.setName("baeldung");

        final Response response = withRequestBody(givenAuth(), feed).post(urlPrefix + "/myFeeds");

        return objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).reader().forType(FeedDto.class).readValue(response.asString());
    }

}
