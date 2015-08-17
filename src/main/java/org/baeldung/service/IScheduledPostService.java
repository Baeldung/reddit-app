package org.baeldung.service;

import java.text.ParseException;
import java.util.List;

import org.baeldung.persistence.model.Post;
import org.baeldung.web.PagingInfo;

public interface IScheduledPostService {
    Post schedulePost(boolean isSuperUser, Post post, boolean resubmitOptionsActivated) throws ParseException;

    void updatePost(final boolean isSuperUser, final Post post, boolean resubmitOptionsActivated) throws ParseException;

    Post getPostById(Long id);

    void deletePostById(Long id);

    List<Post> getPostsList(int page, int size, String sortDir, String sort);

    PagingInfo generatePagingInfo(int page, int size);
}
