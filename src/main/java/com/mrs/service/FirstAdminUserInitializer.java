package com.mrs.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FirstAdminUserInitializer implements CommandLineRunner {
    private final UserService userService;


    public FirstAdminUserInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        userService.createDefaultAdminUser();
    }
}
