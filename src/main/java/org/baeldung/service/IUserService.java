package org.baeldung.service;

import java.util.List;

import org.baeldung.persistence.model.Role;
import org.baeldung.persistence.model.User;
import org.baeldung.web.PagingInfo;

public interface IUserService {

    void registerNewUser(String username, String email, String password, String appUrl);

    List<User> getUsersList(int page, int size, String sortDir, String sort);

    PagingInfo generatePagingInfo(int page, int size);

    List<Role> getRolesList();

    void modifyUserRoles(Long userId, String roleIds);

    User getCurrentUser();

    void setUserEnabled(Long userId, boolean enabled);

    void changeUserPassword(User user, String password);

    boolean checkIfValidOldPassword(User user, String password);

    void resetPassword(String email, String appUrl);

    String checkPasswordResetToken(final long userId, final String token);

    void createVerificationTokenForUser(final User user, final String token);

    String confirmRegistration(final String token);
}
