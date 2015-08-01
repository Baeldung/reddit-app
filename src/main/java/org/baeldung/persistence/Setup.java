package org.baeldung.persistence;

import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.baeldung.persistence.dao.PreferenceRepository;
import org.baeldung.persistence.dao.PrivilegeRepository;
import org.baeldung.persistence.dao.RoleRepository;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.Preference;
import org.baeldung.persistence.model.Privilege;
import org.baeldung.persistence.model.Role;
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
    private CsvDataLoader csvDataLoader;

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
        loadRolesAndPrivileges();
        loadUsers();
    }

    private void loadRolesAndPrivileges() {
        privilegeRepository.save(csvDataLoader.loadObjectList(Privilege.class, SetupData.PRIVILEGES_FILE));
        roleRepository.save(csvDataLoader.loadObjectList(Role.class, SetupData.ROLES_FILE));
        loadRolesPrivilegesRelationship();
    }

    private void loadUsers() {
        final List<User> users = csvDataLoader.loadObjectList(User.class, SetupData.USERS_FILE);
        for (final User user : users) {
            loadUser(user);
        }
        loadUsersRolesRelationship();
    }

    private void loadUser(final User user) {
        try {
            loadUserInternal(user);
        } catch (final Exception e) {
            logger.error("Error occurred while loading user " + user.toString(), e);
        }
    }

    private void loadUserInternal(final User user) {
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

    private void loadRolesPrivilegesRelationship() {
        final List<long[]> rolesPrivileges = csvDataLoader.loadManyToManyRelationship(SetupData.ROLES_PRIVILEGES_FILE);
        for (final long[] arr : rolesPrivileges) {
            final Role role = roleRepository.findOne(arr[0]);
            final Collection<Privilege> privileges = role.getPrivileges();
            privileges.add(privilegeRepository.findOne(arr[1]));
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
    }

    private void loadUsersRolesRelationship() {
        final List<long[]> usersRoles = csvDataLoader.loadManyToManyRelationship(SetupData.USERS_ROLES_FILE);
        for (final long[] arr : usersRoles) {
            final User user = userRepository.findOne(arr[0]);
            final Collection<Role> roles = user.getRoles();
            roles.add(roleRepository.findOne(arr[1]));
            user.setRoles(roles);
            userRepository.save(user);
        }
    }

}
