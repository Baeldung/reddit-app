package org.baeldung.service.impl;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.baeldung.persistence.dao.MyFeedRepository;
import org.baeldung.persistence.model.MyFeed;
import org.baeldung.persistence.model.User;
import org.baeldung.reddit.util.SiteArticle;
import org.baeldung.service.IMyFeedService;
import org.baeldung.web.exceptions.FeedServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

@Service
class MyFeedService implements IMyFeedService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MyFeedRepository myFeedRepository;

    // API

    @Override
    public List<MyFeed> getSitesByUser(final User user) {
        return myFeedRepository.findByUser(user);
    }

    @Override
    public MyFeed saveSite(final MyFeed site) {
        logger.info("New feed {} added", site.toString());
        return myFeedRepository.save(site);
    }

    @Override
    public MyFeed findSiteById(final Long siteId) {
        return myFeedRepository.findOne(siteId);
    }

    @Override
    public void deleteSiteById(final Long siteId) {
        myFeedRepository.delete(siteId);
    }

    @Override
    public List<SiteArticle> getArticlesFromSite(final Long siteId) {
        final MyFeed site = myFeedRepository.findOne(siteId);
        return getArticlesFromSite(site);
    }

    @Override
    public List<SiteArticle> getArticlesFromSite(final MyFeed site) {
        List<SyndEntry> entries;
        try {
            entries = getFeedEntries(site.getUrl());
        } catch (final Exception e) {
            throw new FeedServerException("Error Occurred while parsing feed", e);
        }
        return parseFeed(entries);
    }

    @Override
    public boolean isValidFeedUrl(final String feedUrl) {
        try {
            return getFeedEntries(feedUrl).size() > 0;
        } catch (final Exception e) {
            logger.error("Invalid feed url", e);
            return false;
        }
    }

    // Non API

    private List<SyndEntry> getFeedEntries(final String feedUrl) throws IllegalArgumentException, FeedException, IOException {
        final URL url = new URL(feedUrl);
        final SyndFeed feed = new SyndFeedInput().build(new XmlReader(url));
        logger.info("Read feed from url : {}", feedUrl);
        final List<SyndEntry> entries = feed.getEntries();
        logger.info("{} entries extracted from url {}", entries.size(), feedUrl);
        return entries;
    }

    private List<SiteArticle> parseFeed(final List<SyndEntry> entries) {
        final List<SiteArticle> articles = new ArrayList<SiteArticle>();
        for (final SyndEntry entry : entries) {
            articles.add(new SiteArticle(entry.getTitle(), entry.getLink(), entry.getPublishedDate()));
        }
        return articles;
    }

}
