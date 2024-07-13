package com.security.security_service.service;

import com.security.security_service.model.User;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    User registerUser(User user);
    ResponseEntity<String> loginUser(User user);
    String generateToken(String login);
    void validateToken(String token);
}
