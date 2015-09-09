package org.baeldung.service.query;

import java.util.List;

import org.baeldung.persistence.model.Post;
import org.baeldung.persistence.model.User;
import org.baeldung.web.PagingInfo;

public interface IScheduledPostQueryService {

    Post getPostById(Long id);

    List<Post> getPostsList(User user, int page, int size, String sortDir, String sort);

    PagingInfo generatePagingInfo(User user, int page, int size);

    long countScheduledPostsByUser(User user);

    int countAvailablePostsToSchedule(User user);
}
