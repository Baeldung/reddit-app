package org.baeldung.web.controller.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateUtils;
import org.baeldung.persistence.dao.PostRepository;
import org.baeldung.persistence.model.Post;
import org.baeldung.persistence.model.User;
import org.baeldung.service.IUserService;
import org.baeldung.web.exceptions.InvalidDateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/api/scheduledPosts")
public class ScheduledPostRestController {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final int PAGE_SIZE = 2;
    private static final int LIMIT_SCHEDULED_POSTS_PER_DAY = 3;

    @Autowired
    private PostRepository postReopsitory;

    @Autowired
    private IUserService userService;

    // === API Methods

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public final List<Post> getScheduledPosts(@RequestParam(value = "page", required = false) final int page) {
        final User user = userService.getCurrentUser();
        final Page<Post> posts = postReopsitory.findByUser(user, new PageRequest(page, PAGE_SIZE));
        return posts.getContent();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Post schedule(final HttpServletRequest request, @RequestBody final Post post, @RequestParam(value = "date") final String date) throws ParseException {
        final Date submissionDate = calculateSubmissionDate(date, userService.getCurrentUser().getPreference().getTimezone());
        if (submissionDate.before(new Date())) {
            throw new InvalidDateException("Scheduling Date already passed");
        }
        if (!checkIfCanSchedule(submissionDate, request)) {
            throw new InvalidDateException("Scheduling Date exceeds daily limit");
        }
        post.setSubmissionDate(submissionDate);
        post.setUser(userService.getCurrentUser());
        post.setSubmissionResponse("Not sent yet");
        return postReopsitory.save(post);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Post getPost(@PathVariable("id") final Long id) {
        return postReopsitory.findOne(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updatePost(final HttpServletRequest request, @RequestBody final Post post, @RequestParam(value = "date") final String date) throws ParseException {
        final Date submissionDate = calculateSubmissionDate(date, userService.getCurrentUser().getPreference().getTimezone());
        if (submissionDate.before(new Date())) {
            throw new InvalidDateException("Scheduling Date already passed");
        }
        if (!checkIfCanSchedule(submissionDate, request)) {
            throw new InvalidDateException("Scheduling Date exceeds daily limit");
        }
        post.setSubmissionDate(submissionDate);
        post.setUser(userService.getCurrentUser());
        postReopsitory.save(post);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable("id") final Long id) {
        postReopsitory.delete(id);
    }

    // === private

    private synchronized final Date calculateSubmissionDate(final String dateString, final String userTimeZone) throws ParseException {
        dateFormat.setTimeZone(TimeZone.getTimeZone(userTimeZone));
        return dateFormat.parse(dateString);
    }

    private boolean checkIfCanSchedule(final Date date, final HttpServletRequest request) {
        if (request.isUserInRole("POST_UNLIMITED_PRIVILEGE")) {
            return true;
        }
        final Date start = DateUtils.truncate(date, Calendar.DATE);
        final Date end = DateUtils.addDays(start, 1);
        final long count = postReopsitory.countByUserAndSubmissionDateBetween(userService.getCurrentUser(), start, end);
        return count < LIMIT_SCHEDULED_POSTS_PER_DAY;
    }

}
