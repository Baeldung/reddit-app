package org.baeldung.web.controller.rest;

import java.util.List;

import org.baeldung.persistence.model.MyFeed;
import org.baeldung.reddit.util.SiteArticle;
import org.baeldung.service.IMyFeedService;
import org.baeldung.service.IUserService;
import org.baeldung.web.exceptions.FeedServerException;
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
@RequestMapping(value = "/sites")
class MyFeedRestController {

    @Autowired
    private IMyFeedService myFeedService;

    @Autowired
    private IUserService userService;

    // === API Methods

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<MyFeed> getSitesList() {
        return myFeedService.getSitesByUser(userService.getCurrentUser());
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public MyFeed addSite(@RequestBody final MyFeed site) {
        if (!myFeedService.isValidFeedUrl(site.getUrl())) {
            throw new FeedServerException("Invalid Feed Url");
        }
        site.setUser(userService.getCurrentUser());
        return myFeedService.saveSite(site);
    }

    @RequestMapping(value = "/articles")
    @ResponseBody
    public List<SiteArticle> getSiteArticles(@RequestParam("id") final Long siteId) {
        return myFeedService.getArticlesFromSite(siteId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSite(@PathVariable("id") final Long id) {
        myFeedService.deleteSiteById(id);
    }

}
