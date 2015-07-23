package org.baeldung.service.impl;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.baeldung.persistence.model.Site;
import org.baeldung.reddit.util.SiteArticle;
import org.baeldung.service.ISiteService;
import org.baeldung.service.impl.SiteService;
import org.junit.Before;
import org.junit.Test;

public class SiteUnitTest {

    private ISiteService service;

    @Before
    public void init() {
        service = new SiteService();
    }

    @Test
    public void whenUsingServiceToReadWordpressFeed_thenCorrect() {
        final Site site = new Site("http://www.baeldung.com/feed/");
        final List<SiteArticle> articles = service.getArticlesFromSite(site);

        assertNotNull(articles);
        for (final SiteArticle article : articles) {
            assertNotNull(article.getTitle());
            assertNotNull(article.getLink());
            System.out.println("Title: " + article.getTitle() + ", Link: " + article.getLink());
        }
    }

    @Test
    public void whenUsingRomeToReadBloggerFeed_thenCorrect() {
        final Site site = new Site("http://dohaesam.blogspot.com/feeds/posts/default");
        final List<SiteArticle> articles = service.getArticlesFromSite(site);

        assertNotNull(articles);
        for (final SiteArticle article : articles) {
            assertNotNull(article.getTitle());
            assertNotNull(article.getLink());
            System.out.println("Title: " + article.getTitle() + ", Link: " + article.getLink());
        }
    }

}
