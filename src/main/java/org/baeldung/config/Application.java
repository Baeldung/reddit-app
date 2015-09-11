package org.baeldung.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.baeldung.web.SessionListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    @Bean
    public ServletRegistrationBean dispatcherServletRegistration() {
        final ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet(), "/");
        final Map<String, String> params = new HashMap<String, String>();
        params.put("contextClass", "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");
        params.put("contextConfigLocation", "org.baeldung.config");
        registration.setInitParameters(params);
        registration.setLoadOnStartup(1);
        return registration;
    }

    @Override
    protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        application.sources(Application.class);
        return application;
    }

    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.addListener(new SessionListener());
        servletContext.addListener(new RequestContextListener());
    }

    public static void main(final String... args) {
        SpringApplication.run(Application.class, args);
    }

}
