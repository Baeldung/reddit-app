package org.baeldung.web.controller;

import org.baeldung.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserMvcController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/user/regitrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(final Model model, @RequestParam("token") final String token) {
        final String result = userService.confirmRegistration(token);
        if (result == null) {
            return "redirect:/?msg=Registration confirmed successfully";
        }
        model.addAttribute("msg", result);
        return "submissionResponse";
    }

    @RequestMapping(value = "/user/resetPassword", method = RequestMethod.GET)
    public String resetPassword(final Model model, @RequestParam("id") final long id, @RequestParam("token") final String token) {
        final String result = userService.checkPasswordResetToken(id, token);
        if (result == null) {
            return "updatePassword";
        }
        model.addAttribute("msg", result);
        return "submissionResponse";
    }
}
