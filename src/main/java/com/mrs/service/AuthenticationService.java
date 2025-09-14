package com.mrs.service;

import com.mrs.dto.request.*;
import com.mrs.dto.response.*;

public interface AuthenticationService {
    SuccessResponse userRegistration(ExtendedUserCreateRequest requestDto);

    SuccessOtpVerificationResponse verifyOtp(VerifyOtpRequest requestDto);

    SuccessResponse resendOtp(String email);

    SuccessResponse createPassword(PasswordCreateRequest requestDto);

    UserRegistrationStatus accountStatus(String email);

    LoginResponse userLogin(LoginRequest loginRequest);
}
