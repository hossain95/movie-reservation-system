package com.mrs.dto.request;

import com.mrs.annotation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PasswordCreateRequest {
    @NotBlank
    @NotNull
    @Email
    private String email;

    @NotBlank
    @NotNull
    @ValidPassword
    private String password;

    @NotNull
    @NotBlank(message = "Session id is required")
    private String sessionId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
