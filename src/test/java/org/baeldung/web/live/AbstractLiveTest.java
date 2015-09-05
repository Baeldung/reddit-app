package org.baeldung.web.live;

import org.baeldung.config.TestConfig;
import org.baeldung.reddit.util.Dto;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
public abstract class AbstractLiveTest<D extends Dto> extends AbstractBaseLiveTest {

    protected abstract D newDto() throws Exception;

}
