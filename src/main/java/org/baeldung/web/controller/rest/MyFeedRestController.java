package org.baeldung.web.controller.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.baeldung.persistence.model.MyFeed;
import org.baeldung.reddit.util.FeedArticle;
import org.baeldung.service.IMyFeedService;
import org.baeldung.service.IUserService;
import org.baeldung.web.FeedDto;
import org.baeldung.web.exceptions.FeedServerException;
import org.modelmapper.ModelMapper;
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
@RequestMapping(value = "/myFeeds")
class MyFeedRestController {

    @Autowired
    private IMyFeedService myFeedService;

    @Autowired
    private IUserService userService;

    @Autowired
    private ModelMapper modelMapper;

    // === API Methods

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<FeedDto> getFeedsList() {
        final List<MyFeed> feeds = myFeedService.getFeedsByUser(userService.getCurrentUser());
        return feeds.stream().map(feed -> convertToDto(feed)).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public FeedDto addFeed(@RequestBody final FeedDto feedDto) {
        MyFeed feed = convertToEntity(feedDto);
        if (!myFeedService.isValidFeedUrl(feed.getUrl())) {
            throw new FeedServerException("Invalid Feed Url");
        }
        feed.setUser(userService.getCurrentUser());
        feed = myFeedService.saveFeed(feed);
        return convertToDto(feed);
    }

    @RequestMapping(value = "/articles")
    @ResponseBody
    public List<FeedArticle> getFeedArticles(@RequestParam("id") final Long feedId) {
        return myFeedService.getArticlesFromFeed(feedId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFeed(@PathVariable("id") final Long id) {
        myFeedService.deleteFeedById(id);
    }

    //

    private FeedDto convertToDto(final MyFeed feed) {
        return modelMapper.map(feed, FeedDto.class);
    }

    private MyFeed convertToEntity(final FeedDto feed) {
        return modelMapper.map(feed, MyFeed.class);
    }

}
