package org.baeldung.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.baeldung.persistence.SetupData;
import org.baeldung.persistence.dao.PrivilegeRepository;
import org.baeldung.persistence.dao.RoleRepository;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Setup {

    @Autowired
    private SetupData setupData;

    @Autowired
    private ISetupService setupService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @PostConstruct
    private void setupData() {
        setupRolesAndPrivileges();
        setupUsers();
    }

    private void setupRolesAndPrivileges() {
        privilegeRepository.save(setupData.getPrivileges());
        roleRepository.save(setupData.getRoles());
    }

    private void setupUsers() {
        final List<User> users = setupData.getUsers();
        for (final User user : users) {
            setupService.setupUser(user);
        }
    }

}
