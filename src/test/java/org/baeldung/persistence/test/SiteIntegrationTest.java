package org.baeldung.persistence.test;

import org.baeldung.common.AbstractPersistenceIntegrationTest;
import org.baeldung.persistence.EntityFixtureFactory;
import org.baeldung.persistence.config.TestJpaConfig;
import org.baeldung.persistence.dao.SiteRepository;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.Site;
import org.baeldung.persistence.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestJpaConfig.class }, loader = AnnotationConfigContextLoader.class)
public class SiteIntegrationTest extends AbstractPersistenceIntegrationTest<Site> {

    @Autowired
    private SiteRepository repository;

    @Autowired
    private UserRepository userApi;

    // tests

    @Test
    public final void whenContextIsBootstrapped_thenNoExceptions() {
        //
    }

    // API - protected

    @Override
    protected final SiteRepository getApi() {
        return repository;
    }

    @Override
    protected final void invalidate(final Site entity) {
        entity.setName(null);
    }

    @Override
    protected final Site createNewEntity() {
        final User existingUser = userApi.save(EntityFixtureFactory.newUser());
        return EntityFixtureFactory.newSite(existingUser);
    }

}
