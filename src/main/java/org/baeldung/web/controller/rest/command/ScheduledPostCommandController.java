package org.baeldung.web.controller.rest.command;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.baeldung.persistence.model.Post;
import org.baeldung.persistence.model.User;
import org.baeldung.security.UserPrincipal;
import org.baeldung.service.command.IScheduledPostCommandService;
import org.baeldung.web.dto.command.ScheduledPostAddCommandDto;
import org.baeldung.web.dto.command.ScheduledPostUpdateCommandDto;
import org.baeldung.web.dto.query.ScheduledPostDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/scheduledPosts")
class ScheduledPostCommandController {

    @Autowired
    private IScheduledPostCommandService scheduledPostService;

    @Autowired
    private ModelMapper modelMapper;

    public ScheduledPostCommandController() {
        super();
    }

    // API

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public final ScheduledPostDto schedule(final HttpServletRequest request, @RequestBody final ScheduledPostAddCommandDto postDto) throws ParseException {
        final Post post = convertToEntity(postDto);
        final Post postCreated = scheduledPostService.schedulePost(getCurrentUser(), request.isUserInRole("POST_UNLIMITED_PRIVILEGE"), post, postDto.isResubmitOptionsActivated());
        return convertToDto(postCreated);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public final void updatePost(final HttpServletRequest request, @RequestBody final ScheduledPostUpdateCommandDto postDto) throws ParseException {
        final Post post = convertToEntity(postDto);
        scheduledPostService.updatePost(request.isUserInRole("POST_UNLIMITED_PRIVILEGE"), post, postDto.isResubmitOptionsActivated());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public final void deletePost(@PathVariable("id") final Long id) {
        scheduledPostService.deletePostById(id);
    }

    //

    private ScheduledPostDto convertToDto(final Post post) {
        final ScheduledPostDto postDto = modelMapper.map(post, ScheduledPostDto.class);
        postDto.setSubmissionDate(post.getSubmissionDate(), getCurrentUser().getPreference().getTimezone());
        return postDto;
    }

    private Post convertToEntity(final ScheduledPostAddCommandDto postDto) throws ParseException {
        final Post post = modelMapper.map(postDto, Post.class);
        post.setSubmissionDate(postDto.getSubmissionDateConverted(getCurrentUser().getPreference().getTimezone()));
        return post;
    }

    private Post convertToEntity(final ScheduledPostUpdateCommandDto postDto) throws ParseException {
        final Post post = modelMapper.map(postDto, Post.class);
        post.setSubmissionDate(postDto.getSubmissionDateConverted(getCurrentUser().getPreference().getTimezone()));
        return post;
    }

    private User getCurrentUser() {
        final UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userPrincipal.getUser();
    }

}
