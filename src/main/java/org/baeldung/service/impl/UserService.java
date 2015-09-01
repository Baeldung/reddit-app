package org.baeldung.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.baeldung.persistence.dao.PasswordResetTokenRepository;
import org.baeldung.persistence.dao.PreferenceRepository;
import org.baeldung.persistence.dao.RoleRepository;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.dao.VerificationTokenRepository;
import org.baeldung.persistence.model.PasswordResetToken;
import org.baeldung.persistence.model.Preference;
import org.baeldung.persistence.model.Role;
import org.baeldung.persistence.model.User;
import org.baeldung.persistence.model.VerificationToken;
import org.baeldung.security.UserPrincipal;
import org.baeldung.service.IUserService;
import org.baeldung.service.OnRegistrationCompleteEvent;
import org.baeldung.web.PagingInfo;
import org.baeldung.web.exceptions.UserNotFoundException;
import org.baeldung.web.exceptions.UsernameAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;

@Service
public class UserService implements IUserService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PreferenceRepository preferenceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private Environment env;

    @Override
    public void registerNewUser(final String username, final String email, final String password, final String appUrl) {
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
        preferenceRepository.save(pref);
        user.setPreference(pref);
        final Role role = roleRepository.findByName("ROLE_USER");
        user.setRoles(Sets.newHashSet(role));
        user.setEnabled(false);
        userRepository.save(user);
        logger.info("Register new User {}", user);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, appUrl));
    }

    @Override
    @Transactional
    public List<User> getUsersList(final int page, final int size, final String sortDir, final String sort) {
        final PageRequest pageReq = new PageRequest(page, size, Sort.Direction.fromString(sortDir), sort);
        return userRepository.findAll(pageReq).getContent();
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
        user.setRoles(new HashSet<Role>(roles));
        userRepository.save(user);
    }

    @Override
    public User getCurrentUser() {
        final UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userPrincipal.getUser();
    }

    @Override
    public PagingInfo generatePagingInfo(final int page, final int size) {
        return new PagingInfo(page, size, userRepository.count());
    }

    @Override
    public void setUserEnabled(final Long userId, final boolean enabled) {
        final User user = userRepository.findOne(userId);
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    @Override
    public void changeUserPassword(final User user, final String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public void resetPassword(final String userEmail, final String appUrl) {
        final Preference preference = preferenceRepository.findByEmail(userEmail);
        final User user = userRepository.findByPreference(preference);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        final String token = UUID.randomUUID().toString();
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(myToken);
        final SimpleMailMessage email = constructResetTokenEmail(appUrl, token, user);
        mailSender.send(email);
    }

    @Override
    public String checkPasswordResetToken(final long userId, final String token) {
        final PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);
        if ((passToken == null) || (passToken.getUser().getId() != userId)) {
            return "Invalid Token";
        }

        final Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return "Token Expired";
        }

        final UserPrincipal userPrincipal = new UserPrincipal(passToken.getUser());
        final Authentication auth = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return null;
    }

    @Override
    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public String confirmRegistration(final String token) {
        final VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return "Invalid Token";
        }

        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return "Token Expired";
        }

        final User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        return null;
    }

    //
    private final SimpleMailMessage constructResetTokenEmail(final String contextPath, final String token, final User user) {
        final String url = contextPath + "/user/resetPassword?id=" + user.getId() + "&token=" + token;
        final String message = "Reset your password from ";
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getPreference().getEmail());
        email.setSubject("Reset Password");
        email.setText(message + " \r\n" + url);
        email.setFrom(env.getProperty("support.email"));
        logger.info("Reset password Url : {}", email.getText());
        return email;
    }
}
