package com.wayz.controller;

import com.wayz.config.security.CustomUserDetailsService;
import com.wayz.config.security.JwtUtil;
import com.wayz.config.security.mode.AuthenticationResponse;
import com.wayz.model.User;
import com.wayz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/auth")
public class RegistrationController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final CustomUserDetailsService userDetailsService;

    public RegistrationController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody User user) {
        userService.registerUser(user);
        return new ResponseEntity<>("Registered new User: " + user, HttpStatus.CREATED);
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody User user) {
//        return ResponseEntity.ok(userService.loginUser(user)).getBody();
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(403).body("Incorrect username or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getLogin());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

}
