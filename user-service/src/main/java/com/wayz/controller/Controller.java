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
public class Controller {

    /**
     * Сервис с логикой управления пользователями
     */
    private final UserService userService;

    public Controller(UserService userService, RestTemplate restTemplate) {
        this.userService = userService;
    }

    /**
     * Запрос на обновление пользователя
     *
     * @param user тело запроса с данными о пользователе
     * @return статус ответа сервера
     */
    @PostMapping("/update")
    public ResponseEntity<User> update(@RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(user));
    }

    /**
     * Запрос на получение пользователя по ID
     *
     * @param id user ID
     * @return пользователь
     */
    @GetMapping("/getUserById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    /**
     * Запрос на получение пользователя по логину
     *
     * @param login логин
     * @return пользователь
     */
    @GetMapping("/getUserByLogin/{login}")
    public ResponseEntity<User> getUserByLogin(@PathVariable String login) {
        return ResponseEntity.ok(userService.getUserByLogin(login));
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
