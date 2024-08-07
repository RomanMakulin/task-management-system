package com.security.security_service.controller;

import com.security.security_service.model.User;
import com.security.security_service.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class AuthInfoController {
    private final AuthService authService;

    public AuthInfoController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Запрос на получение авторизованного пользователя
     *
     * @return объект пользователя
     */
    @GetMapping("/get-auth-user")
    public ResponseEntity<User> getAuthUser() {
        return ResponseEntity.ok(authService.getCurrentUser());
    }

}
