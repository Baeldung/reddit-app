package org.baeldung.web.controller.rest;

import org.baeldung.persistence.dao.PreferenceRepository;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.Preference;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/user/preference")
public class UserController {

    @Autowired
    private PreferenceRepository preferenceReopsitory;

    @Autowired
    private UserRepository userReopsitory;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Preference getUserPreference() {
        return getCurrentUser().getPreference();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updateUserPreference(@RequestBody final Preference pref) {
        preferenceReopsitory.save(pref);
        getCurrentUser().setPreference(pref);
    }

    // === private

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
