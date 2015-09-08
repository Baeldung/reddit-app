package org.baeldung.web.controller.rest;

import javax.servlet.http.HttpServletRequest;

import org.baeldung.persistence.model.User;
import org.baeldung.security.UserPrincipal;
import org.baeldung.service.IUserCommandService;
import org.baeldung.web.UserCommandDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/api/users")
public class UserCommandRestController {

    @Autowired
    private IUserCommandService userService;

    @Autowired
    private ModelMapper modelMapper;

    //

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void register(final HttpServletRequest request, @RequestParam("username") final String username, @RequestParam("email") final String email, @RequestParam("password") final String password) {
        final String appUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        userService.registerNewUser(username, email, password, appUrl);
    }

    @RequestMapping(value = "/passwordChange", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void changeUserPassword(@RequestParam("password") final String password, @RequestParam("oldpassword") final String oldPassword) {
        userService.changeUserPassword(getCurrentUser(), password, oldPassword);
    }

    @RequestMapping(value = "/passwordReset", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void forgetPassword(final HttpServletRequest request, @RequestParam("email") final String email) {
        final String appUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        userService.resetPassword(email, appUrl);
    }

    @RequestMapping(value = "/passwordUpdate", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateUserPassword(@RequestParam("password") final String password) {
        userService.updateUserPassword(getCurrentUser(), password);
    }

    // admin

    @PreAuthorize("hasRole('USER_WRITE_PRIVILEGE')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@RequestBody final UserCommandDto userDto) {
        userService.updateUser(convertToEntity(userDto));
    }

    //

    private User convertToEntity(final UserCommandDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    private User getCurrentUser() {
        final UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userPrincipal.getUser();
    }
}
