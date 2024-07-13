package com.security.security_service.service;

import com.security.security_service.model.User;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    void registerUser(User user);
    ResponseEntity<String> loginUser(User user);
}
