package org.baeldung.service.impl;

import java.util.Calendar;
import java.util.List;

import org.baeldung.persistence.dao.PasswordResetTokenRepository;
import org.baeldung.persistence.dao.RoleRepository;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.dao.VerificationTokenRepository;
import org.baeldung.persistence.model.PasswordResetToken;
import org.baeldung.persistence.model.Role;
import org.baeldung.persistence.model.User;
import org.baeldung.persistence.model.VerificationToken;
import org.baeldung.security.UserPrincipal;
import org.baeldung.service.IUserQueryService;
import org.baeldung.web.PagingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserQueryService implements IUserQueryService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    //

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
    public PagingInfo generatePagingInfo(final int page, final int size) {
        return new PagingInfo(page, size, userRepository.count());
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
    public String checkConfirmRegistrationToken(final String token) {
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

}
