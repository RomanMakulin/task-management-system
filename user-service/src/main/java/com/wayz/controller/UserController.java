package com.wayz.controller;

import com.wayz.model.User;
import com.wayz.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * Основной контроллер управления пользователями
 */
@RestController
@RequestMapping("/users")
public class UserController {

    /**
     * Сервис с логикой управления пользователями
     */
    private final UserService userService;

    public UserController(UserService userService, RestTemplate restTemplate) {
        this.userService = userService;
    }

    /**
     * Обновление пользователя
     *
     * @param user тело запроса с данными о пользователе
     * @return статус ответа сервера
     */
    @PostMapping("/update")
    public ResponseEntity<User> update(@RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }

    /**
     * Удаление пользователя по ID
     *
     * @param id user ID
     * @return статус обработки запроса
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

}
