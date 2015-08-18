package org.baeldung.web;

import java.util.Set;

import org.baeldung.persistence.model.Role;

public class UserDto {
    private Long id;

    private String username;

    private Set<Role> roles;

    //

    public UserDto() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(final Set<Role> roles) {
        this.roles = roles;
    }

    //

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("UserDto [id=").append(id).append(", username=").append(username).append(", roles=").append(roles).append("]");
        return builder.toString();
    }

}
