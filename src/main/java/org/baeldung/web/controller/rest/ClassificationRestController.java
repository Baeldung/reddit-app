package org.baeldung.web.controller.rest;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.baeldung.reddit.classifier.RedditClassifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// @Controller
@RequestMapping(value = "/api/classification")
class ClassificationRestController {
    private final SimpleDateFormat dfHour = new SimpleDateFormat("HH");

    @Autowired
    private RedditClassifier redditClassifier;

    // === API Methods

    @RequestMapping(value = "/predicatePostResponse", method = RequestMethod.POST)
    @ResponseBody
    public final String predicatePostResponse(@RequestParam(value = "title") final String title, @RequestParam(value = "domain") final String domain) {
        final int hour = Integer.parseInt(dfHour.format(new Date()));
        final int result = redditClassifier.classify(redditClassifier.convertPost(title, domain, hour));
        return (result == RedditClassifier.GOOD) ? "{Good Response}" : "{Bad response}";
    }

}
