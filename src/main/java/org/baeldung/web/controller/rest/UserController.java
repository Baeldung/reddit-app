package org.baeldung.web.controller.rest;

import java.text.ParseException;

import org.baeldung.persistence.dao.PreferenceRepository;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.Preference;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class UserController {

    @Autowired
    private PreferenceRepository preferenceReopsitory;

    @Autowired
    private UserRepository userReopsitory;

    @RequestMapping(value = "/user/preference")
    @ResponseBody
    public Preference getUserPreference() {
        Preference pref = getCurrentUser().getPreference();
        if (pref == null) {
            pref = new Preference();
            preferenceReopsitory.save(pref);
            final User user = getCurrentUser();
            getCurrentUser().setPreference(pref);
            userReopsitory.save(user);
        }
        return pref;
    }

    @RequestMapping(value = "/user/preference/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void updatePost(@RequestBody final Preference pref, @PathVariable final Long id) throws ParseException {
        preferenceReopsitory.save(pref);
        final User user = getCurrentUser();
        getCurrentUser().setPreference(pref);
        userReopsitory.save(user);
    }

    // non restful
    @RequestMapping(value = "/user/profile")
    public String showUserProfilePage(final Model model) {
        Preference pref = getCurrentUser().getPreference();
        if (pref == null) {
            pref = new Preference();
            pref = new Preference();
            preferenceReopsitory.save(pref);
            final User user = getCurrentUser();
            getCurrentUser().setPreference(pref);
            userReopsitory.save(user);
        }
        model.addAttribute("pref", pref);
        return "profile";
    }

    // === private

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
