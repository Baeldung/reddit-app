package org.baeldung.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "org.baeldung.reddit.persistence", "org.baeldung.reddit.web" })
public class RedditConfig {

}
