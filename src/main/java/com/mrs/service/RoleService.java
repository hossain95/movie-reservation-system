package com.mrs.service;

import com.mrs.dto.request.RoleCreateRequest;
import com.mrs.dto.request.RoleUpdateRequest;
import com.mrs.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse createRole(RoleCreateRequest roleRequest);

    List<RoleResponse> getAllRoles();

    RoleResponse updateRoleByName(RoleUpdateRequest roleRequest);

    RoleResponse getRoleByName(String name);
    RoleResponse getRoleById(Integer id);

    void createDefaultRole();
}
