package org.baeldung.web.controller.rest;

import java.util.List;

import org.baeldung.persistence.model.Role;
import org.baeldung.persistence.model.User;
import org.baeldung.persistence.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @PreAuthorize("hasRole('USER_READ_PRIVILEGE')")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUsersList() {
        return service.getUsersList();
    }

    @PreAuthorize("hasRole('USER_WRITE_PRIVILEGE')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void modifyUserRoles(@PathVariable("id") final Long id, @RequestParam(value = "roleIds") final String roleIds) {
        service.modifyUserRoles(id, roleIds);
    }

    @PreAuthorize("hasRole('USER_READ_PRIVILEGE')")
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    @ResponseBody
    public List<Role> getRolesList() {
        return service.getRolesList();
    }

}
