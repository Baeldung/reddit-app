package org.baeldung.web.controller.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.baeldung.persistence.model.Role;
import org.baeldung.persistence.model.User;
import org.baeldung.service.IScheduledPostService;
import org.baeldung.service.IUserService;
import org.baeldung.web.UserDto;
import org.baeldung.web.exceptions.InvalidOldPasswordException;
import org.modelmapper.ModelMapper;
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
class UserRestController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IScheduledPostService scheduledPostService;

    @Autowired
    private ModelMapper modelMapper;

    // === API Methods

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void register(final HttpServletRequest request, @RequestParam("username") final String username, @RequestParam("email") final String email, @RequestParam("password") final String password) {
        final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        userService.registerNewUser(username, email, password, appUrl);
    }

    @RequestMapping(value = "/user/changePassword", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void changeUserPassword(@RequestParam("password") final String password, @RequestParam("oldpassword") final String oldPassword) {
        final User user = userService.getCurrentUser();
        if (!userService.checkIfValidOldPassword(user, oldPassword)) {
            throw new InvalidOldPasswordException("Invalid old password");
        }
        userService.changeUserPassword(user, password);
    }

    @RequestMapping(value = "/user/forgetPassword", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void forgetPassword(final HttpServletRequest request, @RequestParam("email") final String email) {
        final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        userService.resetPassword(email, appUrl);
    }

    @RequestMapping(value = "/user/updatePassword", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void changeUserPassword(@RequestParam("password") final String password) {
        userService.changeUserPassword(userService.getCurrentUser(), password);
    }

    // === Admin

    @PreAuthorize("hasRole('ADMIN_READ_PRIVILEGE')")
    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    @ResponseBody
    public List<UserDto> getUsersList(@RequestParam(value = "page", required = false, defaultValue = "0") final int page, @RequestParam(value = "size", required = false, defaultValue = "10") final int size,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") final String sortDir, @RequestParam(value = "sort", required = false, defaultValue = "username") final String sort, final HttpServletResponse response) {
        response.addHeader("PAGING_INFO", userService.generatePagingInfo(page, size).toString());
        final List<User> users = userService.getUsersList(page, size, sortDir, sort);
        return users.stream().map(user -> convertUserEntityToDto(user)).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN_READ_PRIVILEGE')")
    @RequestMapping(value = "/admin/roles", method = RequestMethod.GET)
    @ResponseBody
    public List<Role> getRolesList() {
        return userService.getRolesList();
    }

    @PreAuthorize("hasRole('ADMIN_WRITE_PRIVILEGE')")
    @RequestMapping(value = "/user/{id}/role", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void modifyUserRoles(@PathVariable("id") final Long id, @RequestParam(value = "roleIds") final String roleIds) {
        userService.modifyUserRoles(id, roleIds);
    }

    @PreAuthorize("hasRole('ADMIN_WRITE_PRIVILEGE')")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void setUserEnabled(@PathVariable("id") final Long id, @RequestParam(value = "enabled") final boolean enabled) {
        userService.setUserEnabled(id, enabled);
    }

    //

    private UserDto convertUserEntityToDto(final User user) {
        final UserDto dto = modelMapper.map(user, UserDto.class);
        dto.setScheduledPostsCount(scheduledPostService.countScheduledPostsByUser(user));
        return dto;
    }
}
