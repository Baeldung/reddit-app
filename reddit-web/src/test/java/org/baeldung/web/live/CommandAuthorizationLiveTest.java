package org.baeldung.web.live;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.ParseException;

import org.baeldung.web.dto.query.ScheduledPostDto;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.authentication.FormAuthConfig;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

public class CommandAuthorizationLiveTest extends ScheduledPostLiveTest {

    @Test
    public void givenPostOwner_whenUpdatingScheduledPost_thenUpdated() throws ParseException, IOException {
        final ScheduledPostDto post = newDto();
        post.setTitle("new title");
        Response response = withRequestBody(givenAuth(), post).put(urlPrefix + "/api/scheduledPosts/" + post.getId());

        assertEquals(200, response.statusCode());
        response = givenAuth().get(urlPrefix + "/api/scheduledPosts/" + post.getId());
        assertTrue(response.asString().contains(post.getTitle()));
    }

    @Test
    public void givenUserOtherThanOwner_whenUpdatingScheduledPost_thenForbidden() throws ParseException, IOException {
        final ScheduledPostDto post = newDto();
        post.setTitle("new title");
        final Response response = withRequestBody(givenAnotherUserAuth(), post).put(urlPrefix + "/api/scheduledPosts/" + post.getId());

        assertEquals(403, response.statusCode());
    }

    private RequestSpecification givenAnotherUserAuth() {
        final FormAuthConfig formConfig = new FormAuthConfig(urlPrefix + "/j_spring_security_check", "username", "password");
        return RestAssured.given().auth().form("test", "test", formConfig);

    }

}
