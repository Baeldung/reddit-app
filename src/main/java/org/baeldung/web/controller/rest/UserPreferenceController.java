package org.baeldung.web.controller.rest;

import org.baeldung.persistence.dao.PreferenceRepository;
import org.baeldung.persistence.model.Preference;
import org.baeldung.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(value = "/user/preference")
public class UserPreferenceController {

    @Autowired
    private PreferenceRepository preferenceReopsitory;

    @Autowired
    private IUserService userService;

    //

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Preference getCurrentUserPreference() {
        return userService.getCurrentUser().getPreference();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody final Preference pref) {
        preferenceReopsitory.save(pref);
        userService.getCurrentUser().setPreference(pref);
    }

}
