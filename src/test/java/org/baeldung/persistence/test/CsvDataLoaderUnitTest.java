package org.baeldung.persistence.test;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.baeldung.config.PersistenceJpaConfig;
import org.baeldung.persistence.CsvDataLoader;
import org.baeldung.persistence.model.Privilege;
import org.baeldung.persistence.model.Role;
import org.baeldung.persistence.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceJpaConfig.class }, loader = AnnotationConfigContextLoader.class)
public class CsvDataLoaderUnitTest {

    @Autowired
    private CsvDataLoader csvDataLoader;

    @Test
    public void whenLoadUsersFromCsvFile_thenLoaded() {
        final List<User> users = csvDataLoader.loadObjectList(User.class, CsvDataLoader.USERS_FILE);
        assertFalse(users.isEmpty());
    }

    @Test
    public void whenLoadRolesFromCsvFile_thenLoaded() {
        final List<Role> roles = csvDataLoader.loadObjectList(Role.class, CsvDataLoader.ROLES_FILE);
        assertFalse(roles.isEmpty());
    }

    @Test
    public void whenLoadPrivilegesFromCsvFile_thenLoaded() {
        final List<Privilege> privileges = csvDataLoader.loadObjectList(Privilege.class, CsvDataLoader.PRIVILEGES_FILE);
        assertFalse(privileges.isEmpty());
    }

    @Test
    public void whenLoadUsersRolesRelationFromCsvFile_thenLoaded() {
        final List<long[]> usersRoles = csvDataLoader.loadManyToManyRelationship(CsvDataLoader.USERS_ROLES_FILE);
        assertFalse(usersRoles.isEmpty());
    }

    @Test
    public void whenLoadRolesPrivilegesRelationFromCsvFile_thenLoaded() {
        final List<long[]> rolesPrivileges = csvDataLoader.loadManyToManyRelationship(CsvDataLoader.ROLES_PRIVILEGES_FILE);
        assertFalse(rolesPrivileges.isEmpty());
    }
}
