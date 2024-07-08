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


    public Controller(UserService userService, RestTemplate restTemplate) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody User user) {
        userService.registerUser(user);
        return new ResponseEntity<>("Registered new User: " + user, HttpStatus.CREATED);
    }

    @GetMapping("/update")
    public ResponseEntity<User> update(@RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/getUserByLogin")
    public ResponseEntity<User> getUserByLogin(@RequestParam String login) {
        return ResponseEntity.ok(userService.getUserByLogin(login));
    }

    // TODO: Реализовать доступ к заказам через сервис пользователей
//    @GetMapping("/{userId}/orders")
//    public ResponseEntity<String> getUserOrders(@PathVariable Long userId) {
//        //
//        return ResponseEntity.ok(response);
//    }

}
