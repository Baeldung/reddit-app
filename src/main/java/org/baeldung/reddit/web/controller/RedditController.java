package org.baeldung.reddit.web.controller;

import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.service.IUserService;
import org.baeldung.reddit.persistence.beans.RedditTemplate;
import org.baeldung.reddit.persistence.service.IRedditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedditController {

    @Autowired
    private RedditTemplate redditTemplate;

    @Autowired
    private UserRepository userReopsitory;

    @Autowired
    private IRedditService service;

    @Autowired
    private IUserService userService;

    @RequestMapping("/")
    public final String homePage() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return "home";
        }
        return "index";
    }

    @RequestMapping("/redditLogin")
    public final String redditLogin() {
        final OAuth2AccessToken token = redditTemplate.getAccessToken();
        service.connectReddit(redditTemplate.needsCaptcha(), token);
        return "redirect:home";
    }

    @RequestMapping("/post")
    public final String showSubmissionForm(final Model model) {
        if (userService.getCurrentUser().getAccessToken() == null) {
            model.addAttribute("msg", "Sorry, You did not connect your account to Reddit yet");
            return "submissionResponse";
        }

        final boolean isCaptchaNeeded = userService.getCurrentUser().isCaptchaNeeded();
        if (isCaptchaNeeded) {
            final String iden = redditTemplate.getNewCaptcha();
            model.addAttribute("iden", iden);
        }

        return "submissionForm";
    }

    //

    // @RequestMapping("*")
    // public final String fallback() {
    // return "home";
    // }

}
