package org.baeldung.web.controller.rest;

import java.util.List;

import javax.validation.Valid;

import org.baeldung.reddit.persistence.beans.RedditTemplate;
import org.baeldung.reddit.persistence.service.IRedditService;
import org.baeldung.reddit.util.PostDto;
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

}
