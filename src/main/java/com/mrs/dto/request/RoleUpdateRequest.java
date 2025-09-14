package com.mrs.dto.request;

import com.mrs.constraints.Permission;

import java.util.Set;

public class RoleUpdateRequest extends RoleCreateRequest {

    public RoleUpdateRequest(String name, String description, Set<Permission> permissions) {
        super(name, description, permissions);
    }
}
