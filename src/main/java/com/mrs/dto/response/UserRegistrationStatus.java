package com.mrs.dto.response;

import com.mrs.enumeration.AccountStatus;
import com.mrs.model.User;

public class UserRegistrationStatus {
    private String email;
    private AccountStatus status;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public static UserRegistrationStatus fromEntity(String email, AccountStatus status) {
        UserRegistrationStatus userRegistrationStatus = new UserRegistrationStatus();
        userRegistrationStatus.setEmail(email);
        userRegistrationStatus.setStatus(status);

        return userRegistrationStatus;
    }
}
