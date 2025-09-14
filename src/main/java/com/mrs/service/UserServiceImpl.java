package com.mrs.service;

import com.mrs.dto.EmailInfo;
import com.mrs.dto.request.ExtendedUserCreateRequest;
import com.mrs.dto.response.SuccessResponse;
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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RedisService redisService;
    private final EmailService emailService;

    @Value("${default.user.email}")
    private String email;

    @Value("${default.user.phone}")
    private String phone;

    @Value("${default.user.name}")
    private String name;

    @Value("${email.create.password.subject}")
    private String passwordCreateSubject;

    public UserServiceImpl(UserRepository userRepository, RedisService redisService, EmailService emailService) {
        this.userRepository = userRepository;
        this.redisService = redisService;
        this.emailService = emailService;
    }

    @Override
    public SuccessResponse addNewAdminUser(ExtendedUserCreateRequest request) {
        findAdminUserAndThrowIfExist(request.getEmail());
        User user = createAdminUser(request);
        userRepository.save(user);

        String sessionId = TokenUtil.generateSessionId();
        redisService.saveSessionId(user.getEmail(), sessionId);
        emailService.sendPasswordCreateMail(new EmailInfo(user.getEmail(), passwordCreateSubject, "Send session with a link. Link will direct the admin user to portal." + sessionId));
        return SuccessResponse.from(HttpStatus.OK, "Sent session id to " + user.getEmail());
    }

    @Override
    public void createDefaultAdminUser() {
        Optional<User> userOptional = userRepository.findUserByEmailAndAccountType(email, AccountType.ORGANIZATION);
        if (userOptional.isPresent()) {
            log.info("User {} already exists.", userOptional.get().getEmail());
            return;
        }
        log.info("Creating default admin user {}", email);
        ExtendedUserCreateRequest userCreate = new ExtendedUserCreateRequest();
        userCreate.setName(name);
        userCreate.setEmail(email);
        userCreate.setPhone(phone);
        userCreate.setAccountType(AccountType.ORGANIZATION.name());
        userCreate.setRoleId(1);
        userCreate.setGender(Gender.MALE.name());
        addNewAdminUser(userCreate);
    }

    private void findAdminUserAndThrowIfExist(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            log.info("User already exist with email: {}", email);
            throw new BadRequestException("User already exist by " + email);
        });
    }

    private User createAdminUser(ExtendedUserCreateRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setGender(Enum.valueOf(Gender.class, request.getGender()));
        user.setAccountType(AccountType.ORGANIZATION);
        user.setStatus(AccountStatus.PENDING_PASSWORD_CREATION);
        user.setRoleId(request.getRoleId());

        return user;
    }

}
