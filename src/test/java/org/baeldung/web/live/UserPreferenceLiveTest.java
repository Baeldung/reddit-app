package org.baeldung.web.live;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.baeldung.persistence.model.Preference;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.response.Response;

public class UserPreferenceLiveTest extends AbstractBaseLiveTest {

    // tests

    @Test
    public void whenGettingPrefernce_thenCorrect() {
        final Response response = givenAuth().get(urlPrefix + "/user/preference");

        assertEquals(200, response.statusCode());
        assertTrue(response.as(Preference.class).getEmail().contains("john"));
    }

    @Test
    public void whenUpdattingPrefernce_thenCorrect() throws JsonProcessingException {
        final Preference pref = givenAuth().get(urlPrefix + "/user/preference").as(Preference.class);
        pref.setEmail("john@xxxx.com");
        Response response = withRequestBody(givenAuth(), pref).put(urlPrefix + "/user/preference/" + pref.getId());

        assertEquals(200, response.statusCode());
        response = givenAuth().get(urlPrefix + "/user/preference");

        assertEquals(response.as(Preference.class).getEmail(), pref.getEmail());
    }
}
