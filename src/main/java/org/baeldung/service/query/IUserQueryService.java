package org.baeldung.service.query;

import java.util.List;

import org.baeldung.persistence.model.User;

public interface IUserQueryService {

    List<User> getUsersList(int page, int size, String sortDir, String sort);

    String checkPasswordResetToken(final long userId, final String token);

    String checkConfirmRegistrationToken(final String token);

    long countAllUsers();

}
