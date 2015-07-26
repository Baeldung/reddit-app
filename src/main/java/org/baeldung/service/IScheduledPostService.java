package org.baeldung.service;

import java.text.ParseException;
import java.util.List;

import org.baeldung.persistence.model.Post;
import org.baeldung.web.PagingInfo;
import org.baeldung.web.SimplePost;

public interface IScheduledPostService {
    Post schedulePost(boolean isSuperUser, Post post, String dateStr) throws ParseException;

    void updatePost(final boolean isSuperUser, final Post post, final String dateStr) throws ParseException;

    Post getPostById(Long id);

    void deletePostById(Long id);

    List<SimplePost> getPostsList(int page, int size, String sortDir, String sort);

    PagingInfo generatePagingInfo(int page, int size);
}
