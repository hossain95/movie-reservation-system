package com.mrs.dto.response;

import com.mrs.model.User;

public class LoginResponse {
    private String email;
    private String name;
    private String accessToken;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public static LoginResponse from(User user, String accessToken) {
        LoginResponse dto = new LoginResponse();
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setAccessToken(accessToken);
        return dto;
    }
}
