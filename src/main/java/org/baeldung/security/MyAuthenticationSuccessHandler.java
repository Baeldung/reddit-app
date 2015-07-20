package org.baeldung.security;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication auth) throws IOException, ServletException {
        final Set<String> privieleges = AuthorityUtils.authorityListToSet(auth.getAuthorities());
        if (privieleges.contains("ADMIN_READ_PRIVILEGE")) {
            response.sendRedirect("adminHome");
        } else {
            response.sendRedirect("home");
        }
    }
}
