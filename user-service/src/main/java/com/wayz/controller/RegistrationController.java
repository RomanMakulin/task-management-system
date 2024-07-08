package com.wayz.controller;

import com.wayz.model.User;
import com.wayz.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody User user) {
        userService.registerUser(user);
        return new ResponseEntity<>("Registered new User: " + user, HttpStatus.CREATED);
    }
}
