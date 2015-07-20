package org.baeldung.config;

import java.util.Arrays;
import java.util.Collection;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Setup {
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
        createRoles();
        createTestUser();
    }

    private void createTestUser() {
        final Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        final Role superUserRole = roleRepository.findByName("ROLE_SUPER_USER");
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
            userJohn.setRoles(Arrays.asList(adminRole, superUserRole));
            userRepository.save(userJohn);
        }
    }

    private void createRoles() {
        final Privilege adminReadPrivilege = createPrivilegeIfNotFound("ADMIN_READ_PRIVILEGE");
        final Privilege adminWritePrivilege = createPrivilegeIfNotFound("ADMIN_WRITE_PRIVILEGE");
        final Privilege postLimitedPrivilege = createPrivilegeIfNotFound("POST_LIMITED_PRIVILEGE");
        final Privilege postUnlimitedPrivilege = createPrivilegeIfNotFound("POST_UNLIMITED_PRIVILEGE");

        // == create initial roles
        createRoleIfNotFound("ROLE_ADMIN", Arrays.asList(adminReadPrivilege, adminWritePrivilege));
        createRoleIfNotFound("ROLE_SUPER_USER", Arrays.asList(postUnlimitedPrivilege));
        createRoleIfNotFound("ROLE_USER", Arrays.asList(postLimitedPrivilege));

    }

    @Transactional
    private final Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    private final Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

}
