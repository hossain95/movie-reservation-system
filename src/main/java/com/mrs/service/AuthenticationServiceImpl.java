package com.mrs.service;

import com.mrs.dto.EmailInfo;
import com.mrs.dto.request.ExtendedUserCreateRequest;
import com.mrs.dto.request.LoginRequest;
import com.mrs.dto.request.PasswordCreateRequest;
import com.mrs.dto.request.VerifyOtpRequest;
import com.mrs.dto.response.*;
import com.mrs.enumeration.AccountStatus;
import com.mrs.enumeration.AccountType;
import com.mrs.enumeration.Gender;
import com.mrs.exception.BadRequestException;
import com.mrs.model.User;
import com.mrs.repository.UserRepository;
import com.mrs.utils.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final UserRepository userRepository;
    private final RedisService redisService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RoleService roleService;

    @Value("${email.otp.verification.subject}")
    private String subjectVerifyOtp;

    public AuthenticationServiceImpl(UserRepository userRepository, RedisService redisService, EmailService emailService, PasswordEncoder passwordEncoder, JwtService jwtService, RoleService roleService) {
        this.userRepository = userRepository;
        this.redisService = redisService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.roleService = roleService;
    }

    @Transactional
    @Override
    public SuccessResponse userRegistration(ExtendedUserCreateRequest requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .map(existingUser -> handleExistingUser(existingUser, requestDto))
                .orElseGet(() -> createNewUser(requestDto));

        user = userRepository.save(user);
        saveAndSendOtp(user.getEmail());

        return SuccessResponse.from(HttpStatus.OK, "Sent OTP to " + user.getEmail());
    }

    @Override
    public SuccessOtpVerificationResponse verifyOtp(VerifyOtpRequest verifyOtpRequest) {
        User user = userRepository.findByEmail(verifyOtpRequest.getEmail())
                .orElseThrow(() -> new BadRequestException("User does not exist"));

        redisService.validateOtp(verifyOtpRequest.getEmail(), verifyOtpRequest.getOtp());
        markStatusToPendingPasswordCreation(user);
        userRepository.save(user);

        String sessionId = TokenUtil.generateSessionId();
        saveSessionId(user.getEmail(), sessionId);

        return SuccessOtpVerificationResponse.from(sessionId);
    }

    @Override
    public SuccessResponse resendOtp(String email) {
        log.info("Resending OTP for user: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found with : " + email));

        validateUserStatusForOtpResend(user.getEmail(), user.getStatus());
        preventOtpSpam(email);
        saveAndSendOtp(email);
        log.info("Successfully resent OTP for email: {}", email);

        return SuccessResponse.from(HttpStatus.OK, "OTP resent successful");
    }

    @Override
    public SuccessResponse createPassword(PasswordCreateRequest request) {
        log.info("Create password for user {}", request.getEmail());

        String email = request.getEmail();
        String rawPassword = request.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User does not exit"));

        validateSessionId(request);
        updatePasswordAndMarkStatusToActive(user, rawPassword);
        userRepository.save(user);

        log.info("Password creation successful for user {}", email);
        return SuccessResponse.from(HttpStatus.OK, "Password create successful");
    }

    @Override
    public UserRegistrationStatus accountStatus(String email) {
        return userRepository.findByEmail(email)
                .map(user -> UserRegistrationStatus.fromEntity(email, user.getStatus()))
                .orElse(UserRegistrationStatus.fromEntity(email, AccountStatus.NO_ACCOUNT));
    }

    @Override
    public LoginResponse userLogin(LoginRequest request) {
        log.info("Login request: {}", request);

        User user = userRepository.findUserByEmailAndStatus(request.getEmail(), AccountStatus.ACTIVE)
                .orElseThrow(() -> new BadRequestException("User does not exist"));

        matchPassword(request.getPassword(), user.getPassword());

        RoleResponse responseDto = roleService.getRoleById(user.getRoleId());
        String token = jwtService.generateAccessToken(user, responseDto);

        return LoginResponse.from(user, token);
    }

    private User handleExistingUser(User existingUser, ExtendedUserCreateRequest request) {
        if (existingUser.getStatus() == AccountStatus.ACTIVE) {
            log.error("User already exists with email {}", existingUser.getEmail());
            throw new BadRequestException("User already exists");
        }
        return overrideUser(existingUser, request);
    }

    private void markStatusToPendingPasswordCreation(User user) {
        if (user.getStatus() != AccountStatus.PENDING_OTP_VERIFICATION) {
            log.error("User does not OTP verification stage: {}", user.getEmail());
            throw new BadRequestException("User does not OTP verification stage");
        }
        user.setStatus(AccountStatus.PENDING_PASSWORD_CREATION);
    }

    private void updatePasswordAndMarkStatusToActive(User user, String rawPassword) {
        if (user.getStatus() != AccountStatus.PENDING_PASSWORD_CREATION) {
            log.error("User does not password creation stage: {}", user.getEmail());
            throw new BadRequestException("Invalid request");
        }

        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setStatus(AccountStatus.ACTIVE);
        log.info("Password create and activate user {}", user.getEmail());
    }

    private User createNewUser(ExtendedUserCreateRequest requestDto) {
        log.info("Create new user with : {}", requestDto.getEmail());
        User user = new User();
        return overrideUser(user, requestDto);
    }

    private User overrideUser(User user, ExtendedUserCreateRequest requestDto) {
        user.setName(requestDto.getName());
        user.setGender(Gender.valueOf(requestDto.getGender()));
        user.setEmail(requestDto.getEmail());
        user.setPhone(requestDto.getPhone());
        user.setStatus(AccountStatus.PENDING_OTP_VERIFICATION);
        user.setAccountType(AccountType.valueOf(requestDto.getAccountType()));
        user.setRoleId(requestDto.getRoleId());
        return user;
    }

    private void validateUserStatusForOtpResend(String email, AccountStatus status) {
        if (status != AccountStatus.PENDING_OTP_VERIFICATION) {
            log.info("Invalid OTP resend for user {}", email);
            throw new BadRequestException("Invalid OTP request");
        }
    }

    private void preventOtpSpam(String email) {
        if (redisService.checkOtp(email)) {
            log.info("OTP already sent for user: {}", email);
            throw new BadRequestException("An OTP was send recently");
        }
    }

    private void saveAndSendOtp(String email) {
        String otp = generateOtp();
        redisService.saveOtp(email, otp);
        sendOtp(email, otp);
    }

    private String generateOtp() {
        return String.valueOf(SECURE_RANDOM.nextInt(900000) + 100000);
    }

    private void sendOtp(String email, String otp) {
        EmailInfo emailOtp = new EmailInfo(email, subjectVerifyOtp, "Your verification code is " + otp);
        log.info("Send otp");
        emailService.sendOtpVerificationMail(emailOtp);
    }

    private void saveSessionId(String email, String sessionId) {
        redisService.saveSessionId(email, sessionId);
    }

    private void validateSessionId(PasswordCreateRequest request) {
        redisService.validateSessionId(request.getEmail(), request.getSessionId());
    }

    private void matchPassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new BadRequestException("Email/password is incorrect");
        }
    }
}
