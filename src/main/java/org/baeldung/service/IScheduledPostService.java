package org.baeldung.service;

import java.text.ParseException;
import java.util.List;

import org.baeldung.persistence.model.Post;
import org.baeldung.web.PagingInfo;
import org.baeldung.web.SimplePostDto;

public interface IScheduledPostService {
    Post schedulePost(boolean isSuperUser, Post post, String dateStr, boolean resubmitOptionsActivated) throws ParseException;

    void updatePost(final boolean isSuperUser, final Post post, final String dateStr, boolean resubmitOptionsActivated) throws ParseException;

    Post getPostById(Long id);

    void deletePostById(Long id);

    List<SimplePostDto> getPostsList(int page, int size, String sortDir, String sort);

    PagingInfo generatePagingInfo(int page, int size);
}
