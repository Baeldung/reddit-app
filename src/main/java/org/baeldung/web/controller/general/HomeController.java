package org.baeldung.web.controller.general;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @RequestMapping("/")
    public final String homePage() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return "home";
        }
        return "index";
    }

    @RequestMapping(value = "/url/original")
    @ResponseBody
    public String getOriginalLink(@RequestParam("url") final String sourceUrl) throws IOException {
        final URL url = new URL(sourceUrl);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(false);
        final String originalUrl = connection.getHeaderField("Location");
        connection.disconnect();

        return originalUrl.substring(0, originalUrl.indexOf("?"));
    }

}
