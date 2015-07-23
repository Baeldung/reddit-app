package org.baeldung.service;

import java.util.List;

import org.baeldung.persistence.model.Role;
import org.baeldung.persistence.model.User;

public interface IUserService {

    void registerNewUser(String username, String email, String password);

    List<User> getUsersList();

    List<Role> getRolesList();

    void modifyUserRoles(Long userId, String roleIds);

    User getCurrentUser();
}
