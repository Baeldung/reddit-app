package org.baeldung.persistence.service;

import java.util.List;

import org.baeldung.persistence.model.Site;
import org.baeldung.persistence.model.User;
import org.baeldung.reddit.util.SiteArticle;

public interface ISiteService {

    List<Site> getSitesByUser(final User user);

    void saveSite(final Site site);

    Site findSiteById(final Long siteId);

    void deleteSiteById(final Long siteId);

    List<SiteArticle> getArticlesFromSite(final Long siteId);

    List<SiteArticle> getArticlesFromSite(final Site site);

    boolean isValidFeedUrl(final String feedUrl);

}
