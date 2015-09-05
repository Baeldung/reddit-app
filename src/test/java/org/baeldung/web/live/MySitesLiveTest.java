package org.baeldung.web.live;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.baeldung.persistence.model.Site;
import org.junit.Test;

import com.jayway.restassured.response.Response;

public class MySitesLiveTest extends AbstractBaseLiveTest {

    @Test
    public void whenGettingUserSites_thenCorrect() throws ParseException, IOException {
        createSite();
        final Response response = givenAuth().get(urlPrefix + "/sites");

        assertEquals(200, response.statusCode());
        assertTrue(response.as(List.class).size() > 0);
    }

    @Test
    public void whenGettingSiteArticles_thenCorrect() throws ParseException, IOException {
        final Site site = createSite();
        final Response response = givenAuth().get(urlPrefix + "/sites/articles?id=" + site.getId());

        assertEquals(200, response.statusCode());
        assertTrue(response.as(List.class).size() > 0);
    }

    @Test
    public void whenAddingNewSite_thenCorrect() throws ParseException, IOException {
        final Site site = createSite();

        final Response response = givenAuth().get(urlPrefix + "/sites");
        assertTrue(response.asString().contains(site.getUrl()));
    }

    @Test
    public void whenDeletingSite_thenDeleted() throws ParseException, IOException {
        final Site site = createSite();
        final Response response = givenAuth().delete(urlPrefix + "/sites/" + site.getId());

        assertEquals(204, response.statusCode());
    }

    //

    private Site createSite() throws ParseException, IOException {
        final Site site = new Site("http://www.baeldung.com/feed/");
        site.setName("baeldung");

        final Response response = withRequestBody(givenAuth(), site).post(urlPrefix + "/sites");
        return objectMapper.reader().forType(Site.class).readValue(response.asString());
    }
}
