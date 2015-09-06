package org.baeldung.service;

import java.util.List;

import org.baeldung.persistence.model.MyFeed;
import org.baeldung.persistence.model.User;
import org.baeldung.reddit.util.FeedArticle;

public interface IMyFeedService {

    List<MyFeed> getFeedsByUser(final User user);

    MyFeed saveFeed(final MyFeed feed);

    MyFeed findFeedById(final Long feedId);

    void deleteFeedById(final Long feedId);

    List<FeedArticle> getArticlesFromFeed(final Long feedId);

    List<FeedArticle> getArticlesFromSite(final MyFeed feed);

    boolean isValidFeedUrl(final String feedUrl);

}
