package org.baeldung.web.controller;

import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.User;
import org.baeldung.persistence.service.IRedditService;
import org.baeldung.web.RedditTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.JsonNode;

@Controller
public class RedditController {

    @Autowired
    private RedditTemplate redditTemplate;

    @Autowired
    private UserRepository userReopsitory;

    @Autowired
    private IRedditService service;

    @RequestMapping("/")
    public final String homePage() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return "home";
        }
        return "index";
    }

    @RequestMapping("/login")
    public final String redditLogin() {
        final JsonNode node = redditTemplate.getUserInfo();
        service.loadAuthentication(node.get("name").asText(), redditTemplate.getAccessToken());
        return "redirect:home";
    }

    @RequestMapping("/post")
    public final String showSubmissionForm(final Model model) {
        final boolean isCaptchaNeeded = getCurrentUser().isCaptchaNeeded();
        if (isCaptchaNeeded) {
            final String iden = redditTemplate.getNewCaptcha();
            model.addAttribute("iden", iden);
        }

        return "submissionForm";
    }

    // === private

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
