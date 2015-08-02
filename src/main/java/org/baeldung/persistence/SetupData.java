package org.baeldung.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.baeldung.persistence.model.IEntity;
import org.baeldung.persistence.model.Privilege;
import org.baeldung.persistence.model.Role;
import org.baeldung.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class SetupData {

    public final static String USERS_FILE = "users.csv";
    public final static String ROLES_FILE = "roles.csv";
    public final static String PRIVILEGES_FILE = "privileges.csv";
    public final static String ROLES_PRIVILEGES_FILE = "roles_privileges.csv";
    public final static String USERS_ROLES_FILE = "users_roles.csv";

    @Autowired
    private CsvDataLoader csvDataLoader;

    public List<Privilege> getPrivileges() {
        return csvDataLoader.loadObjectList(Privilege.class, PRIVILEGES_FILE);
    }

    public List<Role> getRoles() {
        final List<Privilege> allPrivileges = getPrivileges();
        final List<Role> roles = csvDataLoader.loadObjectList(Role.class, ROLES_FILE);
        final List<long[]> rolesPrivileges = csvDataLoader.loadManyToManyRelationship(SetupData.ROLES_PRIVILEGES_FILE);

        for (final long[] arr : rolesPrivileges) {
            final Role role = findById(roles, arr[0]);
            Collection<Privilege> privileges = role.getPrivileges();
            if (privileges == null) {
                privileges = new ArrayList<Privilege>();
            }
            privileges.add(findById(allPrivileges, arr[1]));
            role.setPrivileges(privileges);
        }
        return roles;
    }

    public List<User> getUsers() {
        final List<Role> allRoles = getRoles();
        final List<User> users = csvDataLoader.loadObjectList(User.class, SetupData.USERS_FILE);
        final List<long[]> usersRoles = csvDataLoader.loadManyToManyRelationship(SetupData.USERS_ROLES_FILE);

        for (final long[] arr : usersRoles) {
            final User user = findById(users, arr[0]);
            Collection<Role> roles = user.getRoles();
            if (roles == null) {
                roles = new ArrayList<Role>();
            }
            roles.add(findById(allRoles, arr[1]));
            user.setRoles(roles);
        }
        return users;
    }

    //

    private <T extends IEntity> T findById(final List<T> list, final long id) {
        return list.stream().filter(item -> item.getId() == id).findFirst().get();
    }
}
