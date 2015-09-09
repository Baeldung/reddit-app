package org.baeldung.service.command;

import java.text.ParseException;

import org.baeldung.persistence.model.Post;
import org.baeldung.persistence.model.User;

public interface IScheduledPostCommandService {
    Post schedulePost(User user, boolean isSuperUser, Post post, boolean resubmitOptionsActivated) throws ParseException;

    void updatePost(final boolean isSuperUser, final Post post, boolean resubmitOptionsActivated) throws ParseException;

    void deletePostById(Long id);

}
