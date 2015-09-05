package org.baeldung.service;

import java.util.List;

import org.baeldung.persistence.model.MyFeed;
import org.baeldung.persistence.model.User;
import org.baeldung.reddit.util.SiteArticle;

public interface IMyFeedService {

    List<MyFeed> getSitesByUser(final User user);

    MyFeed saveSite(final MyFeed site);

    MyFeed findSiteById(final Long siteId);

    void deleteSiteById(final Long siteId);

    List<SiteArticle> getArticlesFromSite(final Long siteId);

    List<SiteArticle> getArticlesFromSite(final MyFeed site);

    boolean isValidFeedUrl(final String feedUrl);

}
