package com.wayz.controller;

import com.wayz.model.User;
import com.wayz.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/users")
public class Controller {

    private final UserService userService;

    private final RestTemplate restTemplate;

    public Controller(UserService userService, RestTemplate restTemplate) {
        this.userService = userService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody User user) {
        userService.registerUser(user);
        return new ResponseEntity<>("Registered new User: " + user, HttpStatus.CREATED);
    }

    @GetMapping("/update")
    public ResponseEntity<String> update(@RequestBody User user) {
        userService.updateUser(user);
        return new ResponseEntity<>("Updated User: " + user, HttpStatus.OK);
    }

    @GetMapping("/getUserById")
    public ResponseEntity<String> getUserById(@RequestParam Long id) {
        userService.getUserById(id);
        return new ResponseEntity<>("User with id: " + id + " found", HttpStatus.OK);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<String> getUserOrders(@PathVariable Long userId) {
        String url = "http://localhost:8081/orders/user/" + userId;
        String response = restTemplate.getForObject(url, String.class);
        return ResponseEntity.ok(response);
    }

}
