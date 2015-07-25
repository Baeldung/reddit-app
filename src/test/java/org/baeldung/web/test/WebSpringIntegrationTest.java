package org.baeldung.web.test;

import org.baeldung.config.PersistenceJpaConfig;
import org.baeldung.config.RedditConfig;
import org.baeldung.config.ServiceConfig;
import org.baeldung.config.WebConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceJpaConfig.class, ServiceConfig.class, WebConfig.class, RedditConfig.class })
@WebAppConfiguration
public class WebSpringIntegrationTest {

    @Test
    public final void whenContextIsBootstrapped_thenOk() {
        //
    }

}
