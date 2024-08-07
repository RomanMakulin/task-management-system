package com.wayz.controller;

import com.wayz.model.User;
import com.wayz.service.UserInfoService;
import com.wayz.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserInfoController {

    /**
     * Сервис с логикой управления пользователями
     */
    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    /**
     * Запрос на получение пользователя по ID
     *
     * @param id user ID
     * @return пользователь
     */
    @GetMapping("/getUserById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userInfoService.getUserById(id));
    }

    /**
     * Запрос на получение пользователя по логину
     *
     * @param login логин
     * @return пользователь
     */
    @GetMapping("/getUserByLogin/{login}")
    public ResponseEntity<User> getUserByLogin(@PathVariable String login) {
        return ResponseEntity.ok(userInfoService.getUserByLogin(login));
    }
}
