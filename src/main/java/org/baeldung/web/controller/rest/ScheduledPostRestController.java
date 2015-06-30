package org.baeldung.web.controller.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.baeldung.persistence.dao.PostRepository;
import org.baeldung.persistence.model.Post;
import org.baeldung.persistence.model.User;
import org.baeldung.web.exceptions.InvalidDateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private PostRepository postReopsitory;

    // === API Methods

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public final List<Post> getScheduledPosts(@RequestParam(value = "page", required = false) final int page) {
        final User user = getCurrentUser();
        final Page<Post> posts = postReopsitory.findByUser(user, new PageRequest(page, PAGE_SIZE));
        return posts.getContent();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Post schedule(@RequestBody final Post post, @RequestParam(value = "date") final String date) throws ParseException {
        final Date submissionDate = calculateSubmissionDate(date, getCurrentUser().getPreference().getTimezone());
        if (submissionDate.before(new Date())) {
            throw new InvalidDateException("Scheduling Date already passed");
        }

        post.setSubmissionDate(submissionDate);
        post.setUser(getCurrentUser());
        post.setSubmissionResponse("Not sent yet");
        return postReopsitory.save(post);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updatePost(@RequestBody final Post post, @RequestParam(value = "date") final String date) throws ParseException {
        final Date submissionDate = calculateSubmissionDate(date, getCurrentUser().getPreference().getTimezone());
        if (submissionDate.before(new Date())) {
            throw new InvalidDateException("Scheduling Date already passed");
        }

        post.setSubmissionDate(submissionDate);
        post.setUser(getCurrentUser());
        postReopsitory.save(post);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable("id") final Long id) {
        postReopsitory.delete(id);
    }

    // === private

    private final User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private synchronized final Date calculateSubmissionDate(final String dateString, final String userTimeZone) throws ParseException {
        dateFormat.setTimeZone(TimeZone.getTimeZone(userTimeZone));
        return dateFormat.parse(dateString);
    }

}
