package org.baeldung.config;

import org.baeldung.web.SessionListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextListener;

@SpringBootApplication
public class Application {

    @Bean
    public SessionListener sessionListener() {
        return new SessionListener();
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    public static void main(final String... args) {
        SpringApplication.run(Application.class, args);
    }
}
