package org.baeldung.service;

import java.text.ParseException;
import java.util.Map;

import org.baeldung.persistence.model.Post;
import org.baeldung.web.DataTableWrapper;

public interface IScheduledPostService {

    DataTableWrapper constructScheduledPostsDataTable(final Map<String, String> params);

    Post schedulePost(boolean isSuperUser, Post post, String dateStr) throws ParseException;

    void updatePost(final boolean isSuperUser, final Post post, final String dateStr) throws ParseException;

    Post getPostById(Long id);

    void deletePostById(Long id);
}
