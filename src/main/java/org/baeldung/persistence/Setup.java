package org.baeldung.persistence;

import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.baeldung.persistence.dao.PreferenceRepository;
import org.baeldung.persistence.dao.PrivilegeRepository;
import org.baeldung.persistence.dao.RoleRepository;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.Preference;
import org.baeldung.persistence.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Setup {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SetupData setupData;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @PostConstruct
    private void setupData() {
        setupRolesAndPrivileges();
        setupUsers();
    }

    private void setupRolesAndPrivileges() {
        privilegeRepository.save(setupData.getPrivileges());
        roleRepository.save(setupData.getRoles());
    }

    private void setupUsers() {
        final List<User> users = setupData.getUsers();
        for (final User user : users) {
            setupUser(user);
        }
    }

    private void setupUser(final User user) {
        try {
            setupUserInternal(user);
        } catch (final Exception e) {
            logger.error("Error occurred while saving user " + user.toString(), e);
        }
    }

    private void setupUserInternal(final User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPreference(createSimplePreference(user));
        userRepository.save(user);
    }

    private Preference createSimplePreference(final User user) {
        final Preference pref = new Preference();
        pref.setId(user.getId());
        pref.setTimezone(TimeZone.getDefault().getID());
        pref.setEmail(user.getUsername() + "@test.com");
        return preferenceRepository.save(pref);
    }

}
