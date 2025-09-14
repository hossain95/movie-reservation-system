package com.mrs.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.mrs.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JwtFilterChain extends OncePerRequestFilter {
    Logger logger = LoggerFactory.getLogger(JwtFilterChain.class);

    private final JwtService jwtService;

    private final static String AUTHORIZATION_HEADER = "Authorization";
    private final static String AUTHORIZATION_PREFIX = "Bearer";

    public JwtFilterChain(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractAccessToken(request);
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContext context = SecurityContextHolder.getContext();

        DecodedJWT decodedJWT = verifyToken(token);

        if (decodedJWT == null) {
            context.setAuthentication(null);
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = passwordAuthenticationToken(decodedJWT);
        context.setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }


    private String extractAccessToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (header == null || !header.startsWith(AUTHORIZATION_PREFIX)) {
            return null;
        }
        return header.substring(AUTHORIZATION_PREFIX.length() + 1);
    }

    private DecodedJWT verifyToken(String token) {
        try {
            return jwtService.verifyJwt(token);
        } catch (Exception e) {
            logger.error("Fail to verify JWT: {}", e.getMessage());
        }
        return null;
    }


    private UsernamePasswordAuthenticationToken passwordAuthenticationToken(DecodedJWT decodedJWT) {
        String subject = decodedJWT.getSubject();
        String[] permissions = decodedJWT.getClaim("scope").asArray(String.class);
        List<SimpleGrantedAuthority> authorities = Arrays.stream(permissions).map(SimpleGrantedAuthority::new).toList();

        return new UsernamePasswordAuthenticationToken(subject, null, authorities);
    }
}
