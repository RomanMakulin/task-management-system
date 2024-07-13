package com.security.security_service.controller;

import com.security.security_service.dto.AuthRequest;
import com.security.security_service.model.User;
import com.security.security_service.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        return ResponseEntity.ok("User registered: " + authService.registerUser(user));
    }

    @PostMapping("/token")
    public ResponseEntity<String> getToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok(authService.generateToken(authRequest.getLogin()));
        } else return ResponseEntity.badRequest().build();
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
        authService.validateToken(token);
        return ResponseEntity.ok("Token validated: " + token);
    }

}
