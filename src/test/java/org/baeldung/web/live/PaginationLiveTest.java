package org.baeldung.web.live;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.junit.Test;

import com.jayway.restassured.response.Response;

public class PaginationLiveTest extends ScheduledPostLiveTest {

    @Test
    public void givenMoreThanOnePage_whenGettingUserScheduledPosts_thenNextPageExist() throws ParseException, IOException {
        createPost();
        createPost();
        createPost();

        final Response response = givenAuth().params("page", 0, "size", 2).get(urlPrefix + "/api/scheduledPosts");

        assertEquals(200, response.statusCode());
        assertTrue(response.as(List.class).size() > 0);

        final String pagingInfo = response.getHeader("PAGING_INFO");
        final long totalNoRecords = Long.parseLong(pagingInfo.split(",")[0].split("=")[1]);
        final String uriToNextPage = pagingInfo.split(",")[2].replace("uriToNextPage=", "").trim();

        assertTrue(totalNoRecords > 2);
        assertEquals(uriToNextPage, "page=1&size=2");
    }

    @Test
    public void givenMoreThanOnePage_whenGettingUserScheduledPostsForSecondPage_thenCorrect() throws ParseException, IOException {
        createPost();
        createPost();
        createPost();

        final Response response = givenAuth().params("page", 1, "size", 2).get(urlPrefix + "/api/scheduledPosts");

        assertEquals(200, response.statusCode());
        assertTrue(response.as(List.class).size() > 0);

        final String pagingInfo = response.getHeader("PAGING_INFO");
        final long totalNoRecords = Long.parseLong(pagingInfo.split(",")[0].split("=")[1]);
        final String uriToPrevPage = pagingInfo.split(",")[3].replace("uriToPrevPage=", "").trim();

        assertTrue(totalNoRecords > 2);
        assertEquals(uriToPrevPage, "page=0&size=2");
    }
}
