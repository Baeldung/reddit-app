package org.baeldung.web.live;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.TimeZone;

import org.baeldung.config.PersistenceJpaConfig;
import org.baeldung.persistence.dao.PostRepository;
import org.baeldung.persistence.dao.PreferenceRepository;
import org.baeldung.persistence.dao.SiteRepository;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.Preference;
import org.baeldung.persistence.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.authentication.FormAuthConfig;
import com.jayway.restassured.specification.RequestSpecification;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceJpaConfig.class }, loader = AnnotationConfigContextLoader.class)
public class AbstractLiveTest {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PreferenceRepository preferenceReopsitory;

    @Autowired
    private PostRepository postReopsitory;

    @Autowired
    private SiteRepository siteReopsitory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    protected ObjectMapper objectMapper;
    
    protected String urlPrefix;

    private FormAuthConfig formConfig;

    protected User userJohn;

    public AbstractLiveTest() {
        objectMapper = new ObjectMapper().setDateFormat(dateFormat).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Properties props;
        try {
            props = PropertiesLoaderUtils.loadProperties(new ClassPathResource("web-dev.properties"));

            final int port = Integer.parseInt(props.getProperty("http.port"));
            if (port != 80) {
                urlPrefix = props.getProperty("http.protocol") + "://" + props.getProperty("http.host") + ":" + port + "/" + props.getProperty("http.path");
            } else {
                urlPrefix = props.getProperty("http.protocol") + "://" + props.getProperty("http.host") + "/" + props.getProperty("http.path");
            }
            formConfig = new FormAuthConfig(urlPrefix + "/j_spring_security_check", "username", "password");
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Before
    public void setup() {
        userJohn = userRepository.findByUsername("john");
        if (userJohn == null) {
            userJohn = new User();
            userJohn.setUsername("john");
            userJohn.setPassword(passwordEncoder.encode("123"));
            userJohn.setAccessToken("token");
            userRepository.save(userJohn);
            final Preference pref = new Preference();
            pref.setTimezone(TimeZone.getDefault().getID());
            pref.setEmail("john@test.com");
            preferenceReopsitory.save(pref);
            userJohn.setPreference(pref);
            userRepository.save(userJohn);
        }

    }

    @After
    public void cleanup() {
        postReopsitory.delete(postReopsitory.findByUser(userJohn));
        siteReopsitory.delete(siteReopsitory.findByUser(userJohn));
    }

    //
    protected RequestSpecification givenAuth() {
        return RestAssured.given().auth().form("john", "123", formConfig);
    }

    protected RequestSpecification withRequestBody(final RequestSpecification req, final Object obj) throws JsonProcessingException {
        return req.contentType(MediaType.APPLICATION_JSON_VALUE).body(objectMapper.writeValueAsString(obj));
    }

}
