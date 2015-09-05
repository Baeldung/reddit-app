package org.baeldung.service.impl;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.baeldung.persistence.model.MyFeed;
import org.baeldung.reddit.util.SiteArticle;
import org.baeldung.service.IMyFeedService;
import org.baeldung.service.impl.MyFeedService;
import org.junit.Before;
import org.junit.Test;

public class MyFeedUnitTest {

    private IMyFeedService service;

    @Before
    public void init() {
        service = new MyFeedService();
    }

    @Test
    public void whenUsingServiceToReadWordpressFeed_thenCorrect() {
        final MyFeed site = new MyFeed("http://www.baeldung.com/feed/");
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
        final MyFeed site = new MyFeed("http://dohaesam.blogspot.com/feeds/posts/default");
        final List<SiteArticle> articles = service.getArticlesFromSite(site);

        assertNotNull(articles);
        for (final SiteArticle article : articles) {
            assertNotNull(article.getTitle());
            assertNotNull(article.getLink());
            System.out.println("Title: " + article.getTitle() + ", Link: " + article.getLink());
        }
    }

}
