package com.security.security_service.service;

import com.security.security_service.dto.AuthRequest;
import com.security.security_service.model.User;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    User registerUser(User user);
    void validateToken(String token);
    ResponseEntity<String> loginUser(AuthRequest authRequest);
    User getCurrentUser();
}
