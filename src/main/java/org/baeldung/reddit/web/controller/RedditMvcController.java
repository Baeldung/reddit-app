package org.baeldung.reddit.web.controller;

import org.baeldung.reddit.persistence.beans.RedditTemplate;
import org.baeldung.reddit.persistence.service.IRedditService;
import org.baeldung.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RedditMvcController {

    @Autowired
    private RedditTemplate redditTemplate;

    @Autowired
    private IRedditService redditService;

    @Autowired
    private IUserService userService;

    // === API Methods

    @RequestMapping("/redditLogin")
    public final String redditLogin() {
        final OAuth2AccessToken token = redditTemplate.getAccessToken();
        redditService.connectReddit(redditTemplate.needsCaptcha(), token);
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

}
