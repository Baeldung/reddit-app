package org.baeldung.persistence.service;

import java.util.List;

import org.baeldung.persistence.model.Site;
import org.baeldung.persistence.model.User;
import org.baeldung.reddit.util.SiteArticle;

public interface ISiteService {

    List<Site> getSitesByUser(User user);

    void saveSite(Site site);

    Site findSiteById(Long siteId);

    void deleteSiteById(Long siteId);

    List<SiteArticle> getArticlesFromSite(Long siteId);

    List<SiteArticle> getArticlesFromSite(Site site);

    boolean isValidFeedUrl(String feedUrl);

}
