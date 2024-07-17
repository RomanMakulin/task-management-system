package com.security.security_service.service;

import com.security.security_service.dto.AuthRequest;
import com.security.security_service.model.User;
import com.security.security_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public User registerUser(User user) {
        if (userRepository.existsByLogin(user.getLogin())) {
            throw new IllegalArgumentException("Login already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info("User registered successfully: {}", user.getLogin());
        return user;
    }

//    @Override
//    public ResponseEntity<String> loginUser(User user) {
//        Optional<User> existingUser = userRepository.findByLogin(user.getLogin());
//        if (existingUser.isPresent() && passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())) {
//            return ResponseEntity.ok("Login successful. Login: " + user.getLogin());
//        } else {
//            return ResponseEntity.status(403).body("Incorrect login or password");
//        }
//    }

    @Override
    public ResponseEntity<String> loginUser(AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
            return ResponseEntity.ok(generateToken(authRequest.getLogin()));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Invalid login or password");
        }
    }

    public String generateToken(String login) {
        return jwtService.generateToken(login);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}
