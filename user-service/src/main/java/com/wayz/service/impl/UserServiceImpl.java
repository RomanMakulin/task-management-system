package com.wayz.service.impl;

import com.wayz.model.User;
import com.wayz.repository.UserRepository;
import com.wayz.service.UserInfoService;
import com.wayz.service.UserService;
import com.wayz.service.UserUpdateService;

import lombok.Data;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


/**
 * Сервис с логикой для управления пользователями
 */
@Service
@Data
public class UserServiceImpl implements UserService {

    /**
     * Логгер
     */
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * Репозиторий пользователей
     */
    private final UserRepository userRepository;

    private final UserInfoService userInfoService;

    /**
     * Сервис обновления пользователей
     */
    private final UserUpdateService userUpdateServiceImpl;

    public UserServiceImpl(UserRepository userRepository, UserInfoService userInfoService, UserUpdateService userUpdateServiceImpl) {
        this.userRepository = userRepository;
        this.userInfoService = userInfoService;
        this.userUpdateServiceImpl = userUpdateServiceImpl;
    }

    /**
     * Логика обновления пользователя
     *
     * @param userDetails данные для обновления
     */
    @Override
    public User updateUser(User userDetails) {
        return userUpdateServiceImpl.updateUser(userDetails);
    }

    /**
     * Удаление пользователя из системы
     *
     * @param id идентификатор пользователя
     * @return статус выполнения запроса
     */
    @Override
    public ResponseEntity<String> deleteUser(Long id) {
        if (userInfoService.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok("Deleted user with id " + id);
        }
        return ResponseEntity.status(404).body("User with id " + id + " not found");
    }

}
