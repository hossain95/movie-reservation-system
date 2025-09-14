package com.mrs.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.mrs.dto.response.RoleResponse;
import com.mrs.model.User;

public interface JwtService {
    String generateAccessToken(User user, RoleResponse role);

    DecodedJWT verifyJwt(String jwt);
}
