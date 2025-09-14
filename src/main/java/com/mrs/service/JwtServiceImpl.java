package com.mrs.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mrs.constraints.Permission;
import com.mrs.dto.response.RoleResponse;
import com.mrs.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final Long expiresTime = Duration.ofMinutes(5).toMillis();

    public JwtServiceImpl(@Value("${jwt.secret}") String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm)
                .build();
    }

    @Override
    public String generateAccessToken(User user, RoleResponse role) {
        logger.info("Generate access token for email: {}", user.getEmail());

        long currentTime = System.currentTimeMillis();
        Date issuedAt = new Date(currentTime);
        Date expiredAt = new Date(currentTime + expiresTime);

        String[] permissions = role.permissions().stream()
                .map(Permission::name)
                .toArray(String[]::new);

        logger.info("Create access token for {} with permissions : {}", user.getEmail(), permissions);

        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("name", user.getName())
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiredAt)
                .withClaim("role", role.name())
                .withArrayClaim("scope", permissions)
                .sign(algorithm);
    }

    @Override
    public DecodedJWT verifyJwt(String accessToken) {
        logger.info("Verifying access token");

        DecodedJWT decodedJWT = verifier.verify(accessToken);
        String email = decodedJWT.getSubject();
        logger.info("Email: {}", email);
        logger.info("Access token verification success");
        return decodedJWT;
    }
}
