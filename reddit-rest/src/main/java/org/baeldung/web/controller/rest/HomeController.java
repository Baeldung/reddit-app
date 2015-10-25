package org.baeldung.web.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
class HomeController {

    @Autowired
    private RememberMeServices rememberMeService;

    // === API Methods

    @RequestMapping(value = "/loginn")
    @ResponseBody
    public String autoLogin(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("tried to autologin");
        System.out.println(rememberMeService);
        final Authentication auth = rememberMeService.autoLogin(request, response);
        System.out.println(auth);
        auth.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(auth);

        if (request.getHeader("Referer") != null)
            return "redirect:" + request.getHeader("Referer");
        else
            return "";
    }

}
