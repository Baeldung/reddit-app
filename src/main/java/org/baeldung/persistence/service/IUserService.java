package org.baeldung.persistence.service;

public interface IUserService {

    void registerNewUser(String username, String email, String password);
}
