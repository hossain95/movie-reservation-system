package com.mrs.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer implements CommandLineRunner {
    private final RoleService roleService;

    public RoleInitializer(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) {
        roleService.createDefaultRole();
    }
}
