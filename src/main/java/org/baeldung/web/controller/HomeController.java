package org.baeldung.web.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/")
    public final String homePage() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return "home";
        }
        return "index";
    }

}
