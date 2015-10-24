package org.baeldung.config.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.catalina.security.SecurityConfig;
import org.baeldung.config.root.CommonConfig;
import org.baeldung.config.root.PersistenceJpaConfig;
import org.baeldung.config.root.RedditConfig;
import org.baeldung.config.root.ServiceConfig;
import org.baeldung.config.root.WebGeneralConfig;
import org.baeldung.web.common.SessionListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    @Bean
    public ServletRegistrationBean apiServlet() {
        final ServletRegistrationBean registration = new ServletRegistrationBean(new DispatcherServlet(), "/api/*");
        final Map<String, String> params = new HashMap<String, String>();
        params.put("contextClass", "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");
        params.put("contextConfigLocation", "org.baeldung.config.api");
        registration.setInitParameters(params);
        registration.setName("ApiServlet");
        registration.setLoadOnStartup(1);
        return registration;
    }

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        application.sources(Application.class, CommonConfig.class, PersistenceJpaConfig.class, RedditConfig.class, SecurityConfig.class, ServiceConfig.class, WebGeneralConfig.class);
        return application;
    }

    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.addListener(new SessionListener());
        servletContext.addListener(new RequestContextListener());
        servletContext.addListener(new HttpSessionEventPublisher());
    }

    public static void main(final String... args) {
        SpringApplication.run(Application.class, args);
    }

}