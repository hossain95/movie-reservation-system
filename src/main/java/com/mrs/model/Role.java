package com.mrs.model;

import com.mrs.constraints.Permission;
import com.mrs.utils.PermissionSetConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;

import java.util.Set;

@Entity(name = "role")
public class Role extends BaseModel {
    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    @Convert(converter = PermissionSetConverter.class)
    private Set<Permission> permissions;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
