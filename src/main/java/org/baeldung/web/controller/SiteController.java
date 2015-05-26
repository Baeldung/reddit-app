package org.baeldung.web.controller;

import java.text.ParseException;
import java.util.List;

import org.baeldung.persistence.model.Site;
import org.baeldung.persistence.model.User;
import org.baeldung.persistence.service.ISiteService;
import org.baeldung.reddit.util.SiteArticle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class SiteController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ISiteService service;

    @RequestMapping("/sites")
    public final String getUserSites(final Model model) {
        final List<Site> sites = service.getSitesByUser(getCurrentUser());
        model.addAttribute("sites", sites);
        return "siteListView";
    }

    @RequestMapping(value = "/sites", method = RequestMethod.POST)
    public final String addSite(final Model model, @RequestParam("url") final String url, @RequestParam("name") final String name) throws ParseException {
        logger.info("User adding Site with these parameters: Url = " + url + ", name = " + name);

        if (!service.isValidFeedUrl(url)) {
            model.addAttribute("msg", "Invalid Feed Url");
            return "submissionResponse";
        }

        final Site site = new Site();
        site.setName(name);
        site.setUrl(url);
        site.setUser(getCurrentUser());
        service.saveSite(site);

        final List<Site> sites = service.getSitesByUser(getCurrentUser());
        model.addAttribute("sites", sites);
        return "siteListView";
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
