package org.baeldung.web.controller;

import org.baeldung.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PostMvcController {

    @Autowired
    private IUserService userService;

    // API

    @RequestMapping("/postSchedule")
    public final String showSchedulePostForm(final Model model) {
        if (userService.getCurrentUser().getAccessToken() == null) {
            model.addAttribute("msg", "Sorry, You did not connect your account to Reddit yet");
            return "submissionResponse";
        }

        final boolean isCaptchaNeeded = userService.getCurrentUser().isCaptchaNeeded();
        if (isCaptchaNeeded) {
            model.addAttribute("msg", "Sorry, You do not have enought karma");
            return "submissionResponse";
        }
        return "schedulePostForm";
    }

    @RequestMapping(value = "/editPost/{id}", method = RequestMethod.GET)
    public String showEditPostForm() {
        return "editPostForm";
    }

}
