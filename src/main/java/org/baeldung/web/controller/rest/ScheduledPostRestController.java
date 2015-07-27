package org.baeldung.web.controller.rest;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.baeldung.persistence.model.Post;
import org.baeldung.service.IScheduledPostService;
import org.baeldung.web.SimplePostDto;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IScheduledPostService scheduledPostService;

    // === API Methods

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public final List<SimplePostDto> getScheduledPosts(@RequestParam(value = "page", required = false, defaultValue = "0") final int page, @RequestParam(value = "size", required = false, defaultValue = "10") final int size,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") final String sortDir, @RequestParam(value = "sort", required = false, defaultValue = "title") final String sort, final HttpServletResponse response) {
        response.addHeader("PAGING_INFO", scheduledPostService.generatePagingInfo(page, size).toString());
        return scheduledPostService.getPostsList(page, size, sortDir, sort);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Post schedule(final HttpServletRequest request, @RequestBody final Post post, @RequestParam(value = "date") final String date, @RequestParam(value = "resubmitOptionsActivated") final boolean resubmitOptionsActivated) throws ParseException {
        return scheduledPostService.schedulePost(request.isUserInRole("POST_UNLIMITED_PRIVILEGE"), post, date, resubmitOptionsActivated);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Post getPost(@PathVariable("id") final Long id) {
        return scheduledPostService.getPostById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updatePost(final HttpServletRequest request, @RequestBody final Post post, @RequestParam(value = "date") final String date, @RequestParam(value = "resubmitOptionsActivated") final boolean resubmitOptionsActivated) throws ParseException {
        scheduledPostService.updatePost(request.isUserInRole("POST_UNLIMITED_PRIVILEGE"), post, date, resubmitOptionsActivated);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable("id") final Long id) {
        scheduledPostService.deletePostById(id);
    }

}
