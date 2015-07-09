package org.baeldung.config;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.baeldung.persistence.dao.PreferenceRepository;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.Preference;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Setup {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void createTestUser() {
        User userJohn = userRepository.findByUsername("john");
        if (userJohn == null) {
            userJohn = new User();
            userJohn.setUsername("john");
            userJohn.setPassword(passwordEncoder.encode("123"));
            userJohn.setAccessToken("token");
            userRepository.save(userJohn);
            final Preference pref = new Preference();
            pref.setTimezone(TimeZone.getDefault().getID());
            pref.setEmail("john@test.com");
            preferenceRepository.save(pref);
            userJohn.setPreference(pref);
            userRepository.save(userJohn);
        }
    }
}
