package org.baeldung.web.controller.rest.query;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.baeldung.persistence.model.Role;
import org.baeldung.persistence.model.User;
import org.baeldung.service.query.IScheduledPostQueryService;
import org.baeldung.service.query.IUserQueryService;
import org.baeldung.web.dto.query.UserQueryDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/api/users")
public class UserQueryRestController {

    @Autowired
    private IUserQueryService userService;

    @Autowired
    private IScheduledPostQueryService scheduledPostService;

    @Autowired
    private ModelMapper modelMapper;

    @PreAuthorize("hasRole('USER_READ_PRIVILEGE')")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<UserQueryDto> getUsersList(@RequestParam(value = "page", required = false, defaultValue = "0") final int page, @RequestParam(value = "size", required = false, defaultValue = "10") final int size,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") final String sortDir, @RequestParam(value = "sort", required = false, defaultValue = "username") final String sort, final HttpServletResponse response) {
        response.addHeader("PAGING_INFO", userService.generatePagingInfo(page, size).toString());
        final List<User> users = userService.getUsersList(page, size, sortDir, sort);
        return users.stream().map(user -> convertUserEntityToDto(user)).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('USER_READ_PRIVILEGE')")
    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    @ResponseBody
    public List<Role> getRolesList() {
        return userService.getRolesList();
    }

    //

    private UserQueryDto convertUserEntityToDto(final User user) {
        final UserQueryDto dto = modelMapper.map(user, UserQueryDto.class);
        dto.setScheduledPostsCount(scheduledPostService.countScheduledPostsByUser(user));
        return dto;
    }
}
