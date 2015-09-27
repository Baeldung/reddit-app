package org.baeldung.config.root;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.baeldung.web.common.SessionListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.DispatcherServlet;

@EnableScheduling
@EnableAsync
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    @Bean
    public ServletRegistrationBean frontendServlet() {
        final ServletRegistrationBean registration = new ServletRegistrationBean(new DispatcherServlet(), "/*");
        final Map<String, String> params = new HashMap<String, String>();
        params.put("contextClass", "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");
        params.put("contextConfigLocation", "org.baeldung.config.frontend");
        registration.setInitParameters(params);
        registration.setName("FrontendServlet");
        registration.setLoadOnStartup(1);
        return registration;
    }

    @Bean
    public ServletRegistrationBean apiServlet() {
        final ServletRegistrationBean registration = new ServletRegistrationBean(new DispatcherServlet(), "/api/*");
        final Map<String, String> params = new HashMap<String, String>();
        params.put("contextClass", "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");
        params.put("contextConfigLocation", "org.baeldung.config.api");
        registration.setInitParameters(params);
        registration.setName("ApiServlet");
        registration.setLoadOnStartup(2);
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
