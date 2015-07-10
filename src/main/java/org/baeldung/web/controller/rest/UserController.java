package org.baeldung.web.controller.rest;

import org.baeldung.persistence.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private IUserService service;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void register(@RequestParam("username") final String username, @RequestParam("email") final String email, @RequestParam("password") final String password) {
        service.registerNewUser(username, email, password);
    }
}
