package com.mrs.dto.response;

import com.mrs.constraints.Permission;
import com.mrs.model.Role;

import java.util.Set;

public record RoleResponse(int id, String name, String description, Set<Permission> permissions) {

    public static RoleResponse from(Role role) {
        return new RoleResponse(role.getId(), role.getName(), role.getDescription(), role.getPermissions());
    }
}
