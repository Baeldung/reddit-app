package org.baeldung.web.controller.rest.command;

import org.baeldung.persistence.model.MyFeed;
import org.baeldung.persistence.model.User;
import org.baeldung.security.UserPrincipal;
import org.baeldung.service.command.IMyFeedCommanndService;
import org.baeldung.web.dto.command.FeedAddCommandDto;
import org.baeldung.web.dto.query.FeedQueryDto;
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
@RequestMapping(value = "/myFeeds")
class MyFeedCommandRestController {

    @Autowired
    private IMyFeedCommanndService myFeedService;

    @Autowired
    private ModelMapper modelMapper;

    // === API Methods

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public FeedQueryDto addFeed(@RequestBody final FeedAddCommandDto feedDto) {
        final MyFeed feed = convertToEntity(feedDto);
        feed.setUser(getCurrentUser());
        return convertToQueryDto(myFeedService.addFeed(feed));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFeed(@PathVariable("id") final Long id) {
        myFeedService.deleteFeedById(id);
    }

    //

    private FeedQueryDto convertToQueryDto(final MyFeed feed) {
        return modelMapper.map(feed, FeedQueryDto.class);
    }

    private MyFeed convertToEntity(final FeedAddCommandDto feed) {
        return modelMapper.map(feed, MyFeed.class);
    }

    private User getCurrentUser() {
        final UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userPrincipal.getUser();
    }

}
