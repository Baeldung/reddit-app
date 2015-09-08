package org.baeldung.service;

import org.baeldung.persistence.model.User;

public interface IUserCommandService {

    void registerNewUser(String username, String email, String password, String appUrl);

    void changeUserPassword(User user, String password, String oldPassword);

    void updateUserPassword(User user, String password);

    void resetPassword(String email, String appUrl);

    void createVerificationTokenForUser(final User user, final String token);

    void updateUser(User user);

}
