package org.baeldung.web.controller.rest;

import java.text.ParseException;
import java.util.List;

import org.baeldung.persistence.model.Site;
import org.baeldung.persistence.model.User;
import org.baeldung.persistence.service.ISiteService;
import org.baeldung.reddit.util.SiteArticle;
import org.baeldung.web.exceptions.FeedServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/api")
public class SiteRestController {

    @Autowired
    private ISiteService service;

    // === API Methods

    @RequestMapping(value = "/sites", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void addSite(final Site site) throws ParseException {
        if (!service.isValidFeedUrl(site.getUrl())) {
            throw new FeedServerException("Invalid Feed Url");
        }
        site.setUser(getCurrentUser());
        service.saveSite(site);
    }

    @RequestMapping(value = "/sites/list")
    @ResponseBody
    public List<Site> getSitesList() {
        return service.getSitesByUser(getCurrentUser());
    }

    @RequestMapping(value = "/sites/articles")
    @ResponseBody
    public List<SiteArticle> getSiteArticles(@RequestParam("id") final Long siteId) {
        return service.getArticlesFromSite(siteId);
    }

    @RequestMapping(value = "/sites/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteSite(@PathVariable("id") final Long id) {
        service.deleteSiteById(id);
    }

    // === private

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
