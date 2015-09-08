package org.baeldung.service;

import java.util.List;

import org.baeldung.persistence.model.Role;
import org.baeldung.persistence.model.User;
import org.baeldung.web.PagingInfo;

public interface IUserQueryService {

    List<User> getUsersList(int page, int size, String sortDir, String sort);

    PagingInfo generatePagingInfo(int page, int size);

    List<Role> getRolesList();

    String checkPasswordResetToken(final long userId, final String token);

    String checkConfirmRegistrationToken(final String token);

}
