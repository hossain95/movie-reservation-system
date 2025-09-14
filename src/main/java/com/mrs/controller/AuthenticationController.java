package com.mrs.controller;

import com.mrs.dto.request.*;
import com.mrs.dto.response.*;
import com.mrs.enumeration.AccountType;
import com.mrs.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final static Integer userRoleId = 2;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/registration")
    ResponseEntity<SuccessResponse> userRegistration(@Valid @RequestBody UserCreateRequest requestDto) {
        ExtendedUserCreateRequest extendedUserCreateRequest = new ExtendedUserCreateRequest(requestDto, AccountType.USER.name(), userRoleId);
        return new ResponseEntity<>(authenticationService.userRegistration(extendedUserCreateRequest), HttpStatus.CREATED);
    }

    @PostMapping("/verify-otp")
    ResponseEntity<SuccessOtpVerificationResponse> verifyOtp(@RequestBody VerifyOtpRequest request) {
        return new ResponseEntity<>(authenticationService.verifyOtp(request), HttpStatus.OK);
    }

    @PostMapping("/resent-otp/{email}")
    ResponseEntity<SuccessResponse> resentOtp(@PathVariable(name = "email") String email) {
        return new ResponseEntity<>(authenticationService.resendOtp(email), HttpStatus.OK);
    }

    @PostMapping("/password-create")
    ResponseEntity<SuccessResponse> createPassword(@Valid @RequestBody PasswordCreateRequest requestDto) {
        return new ResponseEntity<>(authenticationService.createPassword(requestDto), HttpStatus.OK);
    }

    @GetMapping("/account-status/{email}")
    ResponseEntity<UserRegistrationStatus> accountStatus(@PathVariable(name = "email") String email) {
        return new ResponseEntity<>(authenticationService.accountStatus(email), HttpStatus.OK);
    }

    @PostMapping("/login")
    ResponseEntity<LoginResponse> userLogin(@Valid @RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(authenticationService.userLogin(loginRequest), HttpStatus.OK);
    }

}
