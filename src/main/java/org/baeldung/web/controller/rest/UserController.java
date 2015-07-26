package org.baeldung.web.controller.rest;

import java.util.List;

import org.baeldung.persistence.model.Role;
import org.baeldung.persistence.model.User;
import org.baeldung.service.IUserService;
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
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void register(@RequestParam("username") final String username, @RequestParam("email") final String email, @RequestParam("password") final String password) {
        userService.registerNewUser(username, email, password);
    }

    @PreAuthorize("hasRole('ADMIN_READ_PRIVILEGE')")
    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUsersList() {
        return userService.getUsersList();
    }

    @PreAuthorize("hasRole('ADMIN_READ_PRIVILEGE')")
    @RequestMapping(value = "/admin/roles", method = RequestMethod.GET)
    @ResponseBody
    public List<Role> getRolesList() {
        return userService.getRolesList();
    }

    @PreAuthorize("hasRole('ADMIN_WRITE_PRIVILEGE')")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void modifyUserRoles(@PathVariable("id") final Long id, @RequestParam(value = "roleIds") final String roleIds) {
        userService.modifyUserRoles(id, roleIds);
    }

}
