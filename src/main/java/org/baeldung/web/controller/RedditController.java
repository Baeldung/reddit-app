package org.baeldung.web.controller;

import java.util.Arrays;
import java.util.Map;

import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.User;
import org.baeldung.reddit.util.RedditApiConstants;
import org.baeldung.web.RedditTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;

@Controller
public class RedditController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedditTemplate redditTemplate;

    @Autowired
    private UserRepository userReopsitory;

    @RequestMapping("/")
    public final String homePage() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return "home";
        }
        return "index";
    }

    @RequestMapping("/login")
    public final String redditLogin() {
        final JsonNode node = redditTemplate.getUserInfo();
        loadAuthentication(node.get("name").asText(), redditTemplate.getAccessToken());
        return "redirect:home";
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public final String submit(final Model model, @RequestParam final Map<String, String> formParams) {
        final MultiValueMap<String, String> param1 = constructParams(formParams);

        logger.info("Submitting Link with these parameters: " + param1);
        final JsonNode node = redditTemplate.submitPost(param1);
        logger.info("Submitted Link - Full Response from Reddit: " + node.toString());
        final String responseMsg = parseResponse(node);
        model.addAttribute("msg", responseMsg);
        return "submissionResponse";
    }

    @RequestMapping("/post")
    public final String showSubmissionForm(final Model model) {
        final boolean isCaptchaNeeded = getCurrentUser().isCaptchaNeeded();
        if (isCaptchaNeeded) {
            final String iden = redditTemplate.getNewCaptcha();
            model.addAttribute("iden", iden);
        }
        model.addAttribute("pref", getCurrentUser().getPreference());

        return "submissionForm";
    }

    @RequestMapping(value = "/checkIfAlreadySubmitted", method = RequestMethod.POST)
    @ResponseBody
    public String checkIfAlreadySubmitted(@RequestParam("url") final String url, @RequestParam("sr") final String sr) {
        logger.info("check if already submitted");
        final JsonNode node = redditTemplate.searchForLink(url, sr);
        logger.info(node.toString());
        return node.get("data").get("children").toString();
    }

    @RequestMapping(value = "/subredditAutoComplete")
    @ResponseBody
    public String subredditAutoComplete(@RequestParam("term") final String term) {
        final MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
        param.add("query", term);
        final JsonNode node = redditTemplate.subredditNameSearch(term);
        return node.get("names").toString();
    }

    // === private

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private final MultiValueMap<String, String> constructParams(final Map<String, String> formParams) {
        final MultiValueMap<String, String> param = new LinkedMultiValueMap<String, String>();
        param.add(RedditApiConstants.API_TYPE, "json");
        param.add(RedditApiConstants.KIND, "link");
        param.add(RedditApiConstants.RESUBMIT, "true");
        param.add(RedditApiConstants.THEN, "comments");
        for (final Map.Entry<String, String> entry : formParams.entrySet()) {
            param.add(entry.getKey(), entry.getValue());
        }
        return param;
    }

    private final String parseResponse(final JsonNode node) {
        String result = "";
        final JsonNode errorNode = node.get("json").get("errors").get(0);
        if (errorNode != null) {
            for (final JsonNode child : errorNode) {
                result = result + child.toString().replaceAll("\"|null", "") + "<br>";
            }
            return result;
        } else {
            if ((node.get("json").get("data") != null) && (node.get("json").get("data").get("url") != null)) {
                return "Post submitted successfully " + node.get("json").get("data").get("url").asText();
            } else {
                return "Error Occurred";
            }
        }
    }

    private void loadAuthentication(final String name, final OAuth2AccessToken token) {
        User user = userReopsitory.findByUsername(name);
        if (user == null) {
            user = new User();
            user.setUsername(name);
        }

        if (redditTemplate.needsCaptcha().equalsIgnoreCase("true")) {
            user.setNeedCaptcha(true);
        } else {
            user.setNeedCaptcha(false);
        }

        user.setAccessToken(token.getValue());
        user.setRefreshToken(token.getRefreshToken().getValue());
        user.setTokenExpiration(token.getExpiration());
        userReopsitory.save(user);

        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, token.getValue(), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}
