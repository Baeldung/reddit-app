package org.baeldung.web.controller;

import org.baeldung.service.query.IUserQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/users")
public class UserMvcController {

    @Autowired
    private IUserQueryService userService;

    @RequestMapping(value = "/regitrationConfirmation", method = RequestMethod.GET)
    public String confirmRegistration(final Model model, @RequestParam("token") final String token) {
        final String result = userService.checkConfirmRegistrationToken(token);
        if (result == null) {
            return "redirect:/?msg=Registration confirmed successfully";
        }
        model.addAttribute("msg", result);
        return "submissionResponse";
    }

    @RequestMapping(value = "/passwordReset", method = RequestMethod.GET)
    public String resetPassword(final Model model, @RequestParam("id") final long id, @RequestParam("token") final String token) {
        final String result = userService.checkPasswordResetToken(id, token);
        if (result == null) {
            return "updatePassword";
        }
        model.addAttribute("msg", result);
        return "submissionResponse";
    }
}
