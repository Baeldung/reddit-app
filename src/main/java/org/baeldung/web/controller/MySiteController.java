package org.baeldung.web.controller;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.baeldung.persistence.dao.MySiteRepository;
import org.baeldung.persistence.model.MySite;
import org.baeldung.persistence.model.User;
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

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

@Controller
public class MySiteController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MySiteRepository reopsitory;

    @RequestMapping("/mysites")
    public final String getMySites(final Model model) {
        final List<MySite> sites = reopsitory.findByUser(getCurrentUser());
        model.addAttribute("sites", sites);
        return "mysiteListView";
    }

    @RequestMapping(value = "/mysites", method = RequestMethod.POST)
    public final String addMySite(final Model model, @RequestParam("url") final String url, @RequestParam("name") final String name) throws ParseException {
        logger.info("User adding MySite with these parameters: Url = " + url + ", name = " + name);

        final MySite site = new MySite();
        site.setName(name);
        site.setUrl(url);
        site.setUser(getCurrentUser());
        reopsitory.save(site);

        final List<MySite> sites = reopsitory.findByUser(getCurrentUser());
        model.addAttribute("sites", sites);
        return "mysiteListView";
    }

    @RequestMapping(value = "/mysites/list")
    @ResponseBody
    public List<MySite> getMySitesList() {
        return reopsitory.findByUser(getCurrentUser());
    }

    @RequestMapping(value = "/mysites/articles")
    @ResponseBody
    public List<SiteArticle> getSiteArticles(@RequestParam("id") final Long siteID) throws IllegalArgumentException, FeedException, IOException {
        final MySite site = reopsitory.findOne(siteID);
        final URL url = new URL(site.getUrl());
        final SyndFeed feed = new SyndFeedInput().build(new XmlReader(url));
        final List<SyndEntry> entries = feed.getEntries();
        final List<SiteArticle> articles = new ArrayList<SiteArticle>();
        for (final SyndEntry entry : entries) {
            articles.add(new SiteArticle(entry.getTitle(), entry.getLink(), entry.getPublishedDate()));
        }
        return articles;
    }

    @RequestMapping(value = "/mysites/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteSite(@PathVariable("id") final Long id) {
        reopsitory.delete(id);
    }

    // === private

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
