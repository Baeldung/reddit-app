package org.baeldung.web.live;

import static org.junit.Assert.assertEquals;

import org.baeldung.persistence.dao.PreferenceRepository;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.Preference;
import org.baeldung.persistence.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.response.Response;

public class UserPreferenceLiveTest extends AbstractLiveTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PreferenceRepository preferenceReopsitory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User userJohn;

    @Before
    public void init() {
        userJohn = userRepository.findByUsername("john");
        if (userJohn == null) {
            userJohn = new User();
            userJohn.setUsername("john");
            userJohn.setPassword(passwordEncoder.encode("123"));
            userJohn.setAccessToken("token");
        }
        Preference pref = userJohn.getPreference();
        if (pref == null) {
            pref = new Preference();
        }
        pref.setEmail("john@test.com");
        preferenceReopsitory.save(pref);
        userJohn.setPreference(pref);
        userRepository.save(userJohn);
    }

    //

    @Test
    public void whenGettingPrefernce_thenCorrect() {
        final Response response = givenAuth().get(URL_PREFIX + "/user/preference");

        assertEquals(200, response.statusCode());
        assertEquals(response.as(Preference.class).getEmail(), userJohn.getPreference().getEmail());
    }

    @Test
    public void whenUpdattingPrefernce_thenCorrect() throws JsonProcessingException {
        final long id = userJohn.getPreference().getId();
        final Preference pref = new Preference();
        pref.setId(id);
        pref.setEmail("john@new.com");
        Response response = withRequestBody(givenAuth(), pref).put(URL_PREFIX + "/user/preference/" + id);

        assertEquals(200, response.statusCode());
        response = givenAuth().get(URL_PREFIX + "/user/preference");
        assertEquals(response.as(Preference.class).getEmail(), pref.getEmail());
    }
}
