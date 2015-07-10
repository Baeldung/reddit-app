package org.baeldung.persistence.service.impl;

import java.util.TimeZone;

import org.baeldung.persistence.dao.PreferenceRepository;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.Preference;
import org.baeldung.persistence.model.User;
import org.baeldung.persistence.service.IUserService;
import org.baeldung.web.exceptions.UsernameAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PreferenceRepository preferenceReopsitory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void registerNewUser(final String username, final String email, final String password) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        final Preference pref = new Preference();
        pref.setTimezone(TimeZone.getDefault().getID());
        pref.setEmail(email);
        preferenceReopsitory.save(pref);
        user.setPreference(pref);
        userRepository.save(user);
        logger.info("Register new User {}", user);
    }
}
