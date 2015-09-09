package org.baeldung.web.live;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.baeldung.web.PreferenceDto;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.response.Response;

public class UserPreferenceLiveTest extends AbstractBaseLiveTest {

    // tests

    @Test
    public void whenGettingPrefernce_thenCorrect() {
        final Response response = givenAuth().get(urlPrefix + "/api/user/preference");

        assertEquals(200, response.statusCode());
        assertTrue(response.as(PreferenceDto.class).getEmail().contains("john"));
    }

    @Test
    public void whenUpdattingPrefernce_thenCorrect() throws JsonProcessingException {
        final PreferenceDto pref = givenAuth().get(urlPrefix + "/api/user/preference").as(PreferenceDto.class);
        pref.setEmail("john@xxxx.com");
        Response response = withRequestBody(givenAuth(), pref).put(urlPrefix + "/api/user/preference/" + pref.getId());

        assertEquals(200, response.statusCode());
        response = givenAuth().get(urlPrefix + "/api/user/preference");

        assertEquals(response.as(PreferenceDto.class).getEmail(), pref.getEmail());
    }
}
