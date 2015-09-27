package org.baeldung.config.root;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({ "org.baeldung.web.common", "org.baeldung.web.exceptions", "org.baeldung.web.metric", "org.baeldung.web.schedule" })
public class WebGeneralConfig {

}
