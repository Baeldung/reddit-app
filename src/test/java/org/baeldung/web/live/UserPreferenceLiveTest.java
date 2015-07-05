package org.baeldung.web.live;

import static org.junit.Assert.assertEquals;

import org.baeldung.persistence.model.Preference;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.response.Response;

public class UserPreferenceLiveTest extends AbstractLiveTest {

    @Test
    public void whenGettingPrefernce_thenCorrect() {
        final Response response = givenAuth().get(URL_PREFIX + "/user/preference");

        assertEquals(200, response.statusCode());
        assertEquals(response.as(Preference.class).getEmail(), userJohn.getPreference().getEmail());
    }

    @Test
    public void whenUpdattingPrefernce_thenCorrect() throws JsonProcessingException {
        final Preference pref = givenAuth().get(URL_PREFIX + "/user/preference").as(Preference.class);
        pref.setEmail("john@xxxx.com");
        Response response = withRequestBody(givenAuth(), pref).put(URL_PREFIX + "/user/preference/" + pref.getId());

        assertEquals(200, response.statusCode());
        response = givenAuth().get(URL_PREFIX + "/user/preference");

        assertEquals(response.as(Preference.class).getEmail(), pref.getEmail());
    }
}
