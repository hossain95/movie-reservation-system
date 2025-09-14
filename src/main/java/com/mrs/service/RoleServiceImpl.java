package com.mrs.service;

import com.mrs.constraints.Permission;
import com.mrs.dto.request.RoleCreateRequest;
import com.mrs.dto.request.RoleUpdateRequest;
import com.mrs.dto.response.RoleResponse;
import com.mrs.exception.BadRequestException;
import com.mrs.model.Role;
import com.mrs.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {
    private final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleResponse createRole(RoleCreateRequest roleRequest) {
        log.info("Create role: {}", roleRequest);
        roleRepository.findByName(roleRequest.getName())
                .ifPresent(role -> {
                    log.error("Role already exist: {}", roleRequest.getName());
                    throw new BadRequestException("Role already exist: " + roleRequest.getName());
                });

        Role role = createNewRole(roleRequest);
        role = roleRepository.save(role);
        return RoleResponse.from(role);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(RoleResponse::from)
                .toList();
    }

    @Override
    public RoleResponse updateRoleByName(RoleUpdateRequest roleRequest) {
        return roleRepository.findByName(roleRequest.getName())
                .map(role -> updateRole(role, roleRequest))
                .map(roleRepository::save)
                .map(RoleResponse::from)
                .orElseThrow(() -> {
                    log.error("Role does not exist: {}", roleRequest.getName());
                    return new BadRequestException("Role does not exist: " + roleRequest.getName());
                });
    }

    @Override
    public RoleResponse getRoleByName(String name) {
        log.info("Role by name: {}", name);

        return roleRepository.findByName(name)
                .map(RoleResponse::from)
                .orElseThrow(() -> {
                    log.error("Role does not exist by name: {}", name);
                    return new BadRequestException("Role does not exist by name: " + name);
                });
    }

    @Override
    public void createDefaultRole() {
        List<Role> roles = roleRepository.findAll();
        if(!roles.isEmpty()) {
            return;
        }
        Set<Permission> adminPermissions = Set.of(Permission.ORGANIZATION_READ, Permission.ORGANIZATION_WRITE);
        Set<Permission> userPermissions = Set.of(Permission.CUSTOMER_DEFAULT);

        RoleCreateRequest adminRole = new RoleCreateRequest("Organization", "Organization role", adminPermissions);
        createRole(adminRole);

        RoleCreateRequest userRole = new RoleCreateRequest("Customer_Default", "Customer default role", userPermissions);
        createRole(userRole);
    }

    @Override
    public RoleResponse getRoleById(Integer id) {
        return roleRepository.findById(id).map(RoleResponse::from)
                .orElseThrow(() -> new BadRequestException("Role does not exist: " + id));
    }

    private Role createNewRole(RoleCreateRequest roleRequest) {
        log.info("Create new role: {}", roleRequest.getName());
        Role role = new Role();
        role.setName(roleRequest.getName());
        role.setDescription(roleRequest.getDescription());
        role.setPermissions(roleRequest.getPermissions());
        return role;
    }

    private Role updateRole(Role role, RoleUpdateRequest roleRequest) {
        role.setDescription(roleRequest.getDescription());
        role.setPermissions(roleRequest.getPermissions());
        return role;
    }
}
