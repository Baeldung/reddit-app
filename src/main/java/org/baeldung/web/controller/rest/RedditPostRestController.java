package org.baeldung.web.controller.rest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.baeldung.persistence.service.IRedditService;
import org.baeldung.reddit.classifier.RedditClassifier;
import org.baeldung.reddit.util.PostDto;
import org.baeldung.web.RedditTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;

@Controller
@RequestMapping(value = "/api/posts")
public class RedditPostRestController {
    private final SimpleDateFormat dfHour = new SimpleDateFormat("HH");

    @Autowired
    private RedditClassifier redditClassifier;

    @Autowired
    private RedditTemplate redditTemplate;

    @Autowired
    private IRedditService service;

    // === API Methods

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public final List<String> submit(@Valid @RequestBody final PostDto postDto) {
        return service.submitPost(postDto);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String checkIfAlreadySubmitted(@RequestParam("url") final String url, @RequestParam("sr") final String sr) {
        final JsonNode node = redditTemplate.searchForLink(url, sr);
        return node.get("data").get("children").toString();
    }

    @RequestMapping(value = "/predicatePostResponse", method = RequestMethod.POST)
    @ResponseBody
    public final String predicatePostResponse(@RequestParam(value = "title") final String title, @RequestParam(value = "domain") final String domain) {
        final int hour = Integer.parseInt(dfHour.format(new Date()));
        final int result = redditClassifier.classify(redditClassifier.convertPost(title, domain, hour));
        return (result == RedditClassifier.GOOD) ? "{Good Response}" : "{Bad response}";
    }
}
