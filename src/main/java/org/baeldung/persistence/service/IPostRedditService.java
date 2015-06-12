package org.baeldung.persistence.service;

import org.baeldung.persistence.model.Post;

public interface IPostRedditService {
    void submitPost(final Post post);

    void checkAndReSubmit(final Post post);

    void checkAndDelete(final Post post);

    int[] getPostScore(final Post post);

    void deletePost(final String redditId);
}
