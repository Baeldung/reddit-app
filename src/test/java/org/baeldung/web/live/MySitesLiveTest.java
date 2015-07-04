package org.baeldung.web.live;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.baeldung.persistence.dao.SiteRepository;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.Site;
import org.baeldung.persistence.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.response.Response;

public class MySitesLiveTest extends AbstractLiveTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User userJohn;

    private List<Site> sites;

    @Before
    public void init() {
        userJohn = userRepository.findByUsername("john");
        if (userJohn == null) {
            userJohn = new User();
            userJohn.setUsername("john");
            userJohn.setPassword(passwordEncoder.encode("123"));
            userJohn.setAccessToken("token");
            userRepository.save(userJohn);
        }
        sites = siteRepository.findByUser(userJohn);
        if (sites.size() == 0) {
            final Site site = new Site("http://www.baeldung.com/feed/");
            site.setName("baeldung");
            site.setUser(userJohn);
            siteRepository.save(site);
        }

    }

    //

    @Test
    public void whenGettingUserSites_thenCorrect() {
        final Response response = givenAuth().get(URL_PREFIX + "/sites");

        assertEquals(200, response.statusCode());
        assertEquals(response.as(List.class).size(), sites.size());
    }

    @Test
    public void whenGettingSiteArticles_thenCorrect() {
        final Response response = givenAuth().get(URL_PREFIX + "/sites/articles?id=" + sites.get(0).getId());

        assertEquals(200, response.statusCode());
        assertTrue(response.as(List.class).size() > 0);
    }

    @Test
    public void whenAddingNewSite_thenCorrect() throws JsonProcessingException {
        final Site site = new Site();
        site.setName("doha");
        site.setUrl("http://dohaesam.blogspot.com/feeds/posts/default");
        Response response = withRequestBody(givenAuth(), site).post(URL_PREFIX + "/sites");

        assertEquals(200, response.statusCode());
        response = givenAuth().get(URL_PREFIX + "/sites");
        assertTrue(response.asString().contains(site.getUrl()));
    }

    @Test
    public void whenDeletingSite_thenDeleted() {
        final Site site = sites.get(0);
        final Response response = givenAuth().delete(URL_PREFIX + "/sites/" + site.getId());

        assertEquals(200, response.statusCode());
        assertNull(siteRepository.findOne(site.getId()));
    }
}
