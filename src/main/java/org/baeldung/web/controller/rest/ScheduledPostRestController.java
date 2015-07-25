package org.baeldung.web.controller.rest;

import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.baeldung.persistence.model.Post;
import org.baeldung.service.IScheduledPostService;
import org.baeldung.web.DataTableWrapper;
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
    private IScheduledPostService service;

    // === API Methods

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public final DataTableWrapper getScheduledPosts(@RequestParam final Map<String, String> params) {
        return service.constructScheduledPostsDataTable(params);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Post schedule(final HttpServletRequest request, @RequestBody final Post post, @RequestParam(value = "date") final String date) throws ParseException {
        return service.schedulePost(request.isUserInRole("POST_UNLIMITED_PRIVILEGE"), post, date);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Post getPost(@PathVariable("id") final Long id) {
        return service.getPostById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updatePost(final HttpServletRequest request, @RequestBody final Post post, @RequestParam(value = "date") final String date) throws ParseException {
        service.updatePost(request.isUserInRole("POST_UNLIMITED_PRIVILEGE"), post, date);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable("id") final Long id) {
        service.deletePostById(id);
    }

}
