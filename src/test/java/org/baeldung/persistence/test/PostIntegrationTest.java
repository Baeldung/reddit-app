package org.baeldung.persistence.test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

import org.baeldung.common.AbstractPersistenceIntegrationTest;
import org.baeldung.config.PersistenceJpaConfig;
import org.baeldung.persistence.dao.PostRepository;
import org.baeldung.persistence.model.Post;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceJpaConfig.class }, loader = AnnotationConfigContextLoader.class)
@Ignore
public class PostIntegrationTest extends AbstractPersistenceIntegrationTest<Post> {

    @Autowired
    private PostRepository repository;

    // tests

    @Test
    public final void whenContextIsBootstrapped_thenNoExceptions() {
        //
    }

    // API - protected

    @Override
    protected final PostRepository getApi() {
        return repository;
    }

    @Override
    protected final void invalidate(final Post entity) {
        entity.setSubreddit(null);
    }

    @Override
    protected final Post createNewEntity() {
        final Post post = new Post();
        post.setSubreddit(randomAlphabetic(6));
        return post;
    }

}
