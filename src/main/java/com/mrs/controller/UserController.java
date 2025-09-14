package com.mrs.controller;

import com.mrs.dto.request.*;
import com.mrs.dto.response.SuccessResponse;
import com.mrs.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN_PERMISSION')")
    @PostMapping("/add")
    ResponseEntity<SuccessResponse> userRegistration(@Valid @RequestBody ExtendedUserCreateRequest requestDto) {
        return new ResponseEntity<>(userService.addNewAdminUser(requestDto), HttpStatus.CREATED);
    }
}
