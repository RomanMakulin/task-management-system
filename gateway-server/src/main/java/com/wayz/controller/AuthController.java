package com.wayz.controller;

import com.wayz.dto.AuthDdo;
import com.wayz.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthDdo authDdo, HttpServletResponse response) {
        if (authService.checkPassword(authDdo)) {
            authService.setCookie(authDdo, response);
            return ResponseEntity.ok("Authentication successful! Login: " + authDdo.getLogin());
        }
        return ResponseEntity.status(403).body("Authentication failed");
    }

}
