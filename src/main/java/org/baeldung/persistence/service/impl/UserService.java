package org.baeldung.persistence.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.baeldung.persistence.dao.PreferenceRepository;
import org.baeldung.persistence.dao.RoleRepository;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.Preference;
import org.baeldung.persistence.model.Role;
import org.baeldung.persistence.model.User;
import org.baeldung.persistence.service.IUserService;
import org.baeldung.security.UserPrincipal;
import org.baeldung.web.exceptions.UsernameAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements IUserService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PreferenceRepository preferenceReopsitory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

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
        final Role role = roleRepository.findByName("ROLE_USER");
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
        logger.info("Register new User {}", user);
    }

    @Override
    @Transactional
    public List<User> getUsersList() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public List<Role> getRolesList() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public void modifyUserRoles(final Long userId, final String ids) {
        final List<Long> roleIds = new ArrayList<Long>();
        final String[] arr = ids.split(",");
        for (final String str : arr) {
            roleIds.add(Long.parseLong(str));
        }
        final List<Role> roles = roleRepository.findAll(roleIds);
        final User user = userRepository.findOne(userId);
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public User getCurrentUser() {
        final UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userPrincipal.getUser();
    }
}
