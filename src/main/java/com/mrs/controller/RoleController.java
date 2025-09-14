package com.mrs.controller;

import com.mrs.dto.request.RoleCreateRequest;
import com.mrs.dto.request.RoleUpdateRequest;
import com.mrs.dto.response.RoleResponse;
import com.mrs.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PreAuthorize("hasAnyAuthority('ORGANIZATION_WRITE','ROLE_WRITE')")
    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody RoleCreateRequest roleCreateRequest) {
        return new ResponseEntity<>(roleService.createRole(roleCreateRequest), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ORGANIZATION_READ','ROLE_READ')")
    @GetMapping("/all")
    public ResponseEntity<List<RoleResponse>> findAllRoles() {
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ORGANIZATION_WRITE','ROLE_WRITE')")
    @PutMapping
    public ResponseEntity<RoleResponse> updateRole(@Valid @RequestBody RoleUpdateRequest requestDot){
        return new ResponseEntity<>(roleService.updateRoleByName(requestDot), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ORGANIZATION_READ','ROLE_WRITE')")
    @GetMapping("/by-name/{name}")
    public ResponseEntity<RoleResponse> getRoleByName(@PathVariable(name = "name") String name){
        return new ResponseEntity<>(roleService.getRoleByName(name), HttpStatus.OK);
    }
}
