package org.baeldung.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.lang.time.DateUtils;
import org.baeldung.persistence.dao.PostRepository;
import org.baeldung.persistence.model.Post;
import org.baeldung.service.IScheduledPostService;
import org.baeldung.service.IUserService;
import org.baeldung.web.PagingInfo;
import org.baeldung.web.SimplePostDto;
import org.baeldung.web.exceptions.InvalidDateException;
import org.baeldung.web.exceptions.InvalidResubmitOptionsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ScheduledPostService implements IScheduledPostService {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final int LIMIT_SCHEDULED_POSTS_PER_DAY = 3;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private IUserService userService;

    //

    @Override
    public List<SimplePostDto> getPostsList(final int page, final int size, final String sortDir, final String sort) {
        final PageRequest pageReq = new PageRequest(page, size, Sort.Direction.fromString(sortDir), sort);
        final Page<Post> posts = postRepository.findByUser(userService.getCurrentUser(), pageReq);
        return constructDataAccordingToUserTimezone(posts.getContent());
    }

    @Override
    public PagingInfo generatePagingInfo(final int page, final int size) {
        final long total = postRepository.countByUser(userService.getCurrentUser());
        return new PagingInfo(page, size, total);
    }

    @Override
    public Post schedulePost(final boolean isSuperUser, final Post post, final boolean resubmitOptionsActivated) throws ParseException {
        if (resubmitOptionsActivated && !checkIfValidResubmitOptions(post)) {
            throw new InvalidResubmitOptionsException("Invalid Resubmit Options");
        }
        if (post.getSubmissionDate().before(new Date())) {
            throw new InvalidDateException("Scheduling Date already passed");
        }
        if (!(isSuperUser || checkIfCanSchedule(post.getSubmissionDate()))) {
            throw new InvalidDateException("Scheduling Date exceeds daily limit");
        }
        post.setUser(userService.getCurrentUser());
        post.setSubmissionResponse("Not sent yet");
        return postRepository.save(post);
    }

    @Override
    public void updatePost(final boolean isSuperUser, final Post post, final boolean resubmitOptionsActivated) throws ParseException {
        if (resubmitOptionsActivated && !checkIfValidResubmitOptions(post)) {
            throw new InvalidResubmitOptionsException("Invalid Resubmit Options");
        }
        if (post.getSubmissionDate().before(new Date())) {
            throw new InvalidDateException("Scheduling Date already passed");
        }
        if (!(isSuperUser || checkIfCanSchedule(post.getSubmissionDate()))) {
            throw new InvalidDateException("Scheduling Date exceeds daily limit");
        }
        post.setUser(userService.getCurrentUser());
        postRepository.save(post);
    }

    @Override
    public Post getPostById(final Long id) {
        return postRepository.findOne(id);
    }

    @Override
    public void deletePostById(final Long id) {
        postRepository.delete(id);
    }

    //

    private boolean checkIfValidResubmitOptions(final Post post) {
        if (checkIfAllNonZero(post.getNoOfAttempts(), post.getTimeInterval(), post.getMinScoreRequired())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkIfAllNonZero(final int... args) {
        for (final int tmp : args) {
            if (tmp == 0) {
                return false;
            }
        }
        return true;
    }

    private String convertToUserTomeZone(final Date date, final String timeZone) {
        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        return dateFormat.format(date);
    }

    private List<SimplePostDto> constructDataAccordingToUserTimezone(final List<Post> posts) {
        final String timeZone = userService.getCurrentUser().getPreference().getTimezone();
        return posts.stream().map(post -> new SimplePostDto(post, convertToUserTomeZone(post.getSubmissionDate(), timeZone))).collect(Collectors.toList());
    }

    private boolean checkIfCanSchedule(final Date date) {
        final Date start = DateUtils.truncate(date, Calendar.DATE);
        final Date end = DateUtils.addDays(start, 1);
        final long count = postRepository.countByUserAndSubmissionDateBetween(userService.getCurrentUser(), start, end);
        return count < LIMIT_SCHEDULED_POSTS_PER_DAY;
    }

}
