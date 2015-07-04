package org.baeldung.web.live;

import org.baeldung.web.live.config.LiveTestConfig;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.authentication.FormAuthConfig;
import com.jayway.restassured.specification.RequestSpecification;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { LiveTestConfig.class }, loader = AnnotationConfigContextLoader.class)
public class AbstractLiveTest {
    public static final String URL_PREFIX = "http://localhost:8080/reddit-scheduler";

    private final FormAuthConfig formConfig = new FormAuthConfig(URL_PREFIX + "/j_spring_security_check", "username", "password");
    private final ObjectMapper objectMapper = new ObjectMapper();

    //

    protected RequestSpecification givenAuth() {
        return RestAssured.given().auth().form("john", "123", formConfig);
    }

    protected RequestSpecification withRequestBody(final RequestSpecification req, final Object obj) throws JsonProcessingException {
        return req.contentType(MediaType.APPLICATION_JSON_VALUE).body(objectMapper.writeValueAsString(obj));
    }

}
