package org.baeldung.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.baeldung.config.CommonConfig;
import org.baeldung.config.PersistenceJpaConfig;
import org.baeldung.config.RedditConfig;
import org.baeldung.config.SecurityConfig;
import org.baeldung.config.ServiceConfig;
import org.baeldung.config.ThymeleafConfig;
import org.baeldung.config.WebConfig;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

public class ServletInitializer extends AbstractDispatcherServletInitializer {

    @Override
    protected WebApplicationContext createServletApplicationContext() {
        final AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(CommonConfig.class, PersistenceJpaConfig.class, ServiceConfig.class, WebConfig.class, SecurityConfig.class, ThymeleafConfig.class, RedditConfig.class);
        return context;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        return null;
    }

    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.addListener(new SessionListener());
        registerProxyFilter(servletContext, "oauth2ClientContextFilter");
        registerProxyFilter(servletContext, "springSecurityFilterChain");
        registerProxyFilter(servletContext, "metricFilter");
    }

    private void registerProxyFilter(final ServletContext servletContext, final String name) {
        final DelegatingFilterProxy filter = new DelegatingFilterProxy(name);
        filter.setContextAttribute("org.springframework.web.servlet.FrameworkServlet.CONTEXT.dispatcher");
        servletContext.addFilter(name, filter).addMappingForUrlPatterns(null, false, "/*");
    }

}
