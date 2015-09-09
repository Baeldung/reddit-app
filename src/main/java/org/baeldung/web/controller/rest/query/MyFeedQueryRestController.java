package org.baeldung.web.controller.rest.query;

import java.util.List;
import java.util.stream.Collectors;

import org.baeldung.persistence.model.MyFeed;
import org.baeldung.persistence.model.User;
import org.baeldung.reddit.util.FeedArticle;
import org.baeldung.security.UserPrincipal;
import org.baeldung.service.query.IMyFeedQueryService;
import org.baeldung.web.dto.query.FeedQueryDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/api/myFeeds")
class MyFeedQueryRestController {

    @Autowired
    private IMyFeedQueryService myFeedService;

    @Autowired
    private ModelMapper modelMapper;

    // === API Methods

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<FeedQueryDto> getFeedsList() {
        final List<MyFeed> feeds = myFeedService.getFeedsByUser(getCurrentUser());
        return feeds.stream().map(feed -> convertToDto(feed)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/articles")
    @ResponseBody
    public List<FeedArticle> getFeedArticles(@RequestParam("id") final Long feedId) {
        return myFeedService.getArticlesFromFeed(feedId);
    }

    //

    private FeedQueryDto convertToDto(final MyFeed feed) {
        return modelMapper.map(feed, FeedQueryDto.class);
    }

    private User getCurrentUser() {
        final UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userPrincipal.getUser();
    }

}
