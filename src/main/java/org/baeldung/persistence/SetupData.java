package org.baeldung.persistence;

public final class SetupData {

    public final static String USERS_FILE = "users.csv";
    public final static String ROLES_FILE = "roles.csv";
    public final static String PRIVILEGES_FILE = "privileges.csv";
    public final static String ROLES_PRIVILEGES_FILE = "roles_privileges.csv";
    public final static String USERS_ROLES_FILE = "users_roles.csv";

    private SetupData() {
        throw new AssertionError();
    }

}
