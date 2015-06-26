package org.baeldung.web.controller;

import java.text.SimpleDateFormat;

import org.baeldung.persistence.dao.PostRepository;
import org.baeldung.persistence.model.Post;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PostMvcController {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Autowired
    private PostRepository postReopsitory;

    // API

    @RequestMapping("/postSchedule")
    public final String showSchedulePostForm(final Model model) {
        final boolean isCaptchaNeeded = getCurrentUser().isCaptchaNeeded();
        if (isCaptchaNeeded) {
            model.addAttribute("msg", "Sorry, You do not have enought karma");
            return "submissionResponse";
        }
        return "schedulePostForm";
    }

    @RequestMapping(value = "/editPost/{id}", method = RequestMethod.GET)
    public String showEditPostForm(final Model model, @PathVariable final Long id) {
        final Post post = postReopsitory.findOne(id);
        model.addAttribute("post", post);
        model.addAttribute("dateValue", dateFormat.format(post.getSubmissionDate()));
        return "editPostForm";
    }

    // === private

    private final User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
