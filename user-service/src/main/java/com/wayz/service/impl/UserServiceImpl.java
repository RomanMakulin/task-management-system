package com.wayz.service.impl;

import com.wayz.model.User;
import com.wayz.repository.UserRepository;
import com.wayz.service.UserService;
import com.wayz.service.UserUpdateService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Data
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final UserUpdateService userUpdateServiceImpl;

    public UserServiceImpl(UserRepository userRepository, UserUpdateService userUpdateServiceImpl) {
        this.userRepository = userRepository;
        this.userUpdateServiceImpl = userUpdateServiceImpl;
    }

    /**
     * Логика регистрации пользователя
     *
     * @param user передаваемый пользователь
     */
    @Override
    public void registerUser(User user) {
        if (userRepository.existsByLogin(user.getLogin())) {
            throw new IllegalArgumentException("Login already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        try {
            userRepository.save(user);
            log.info("User registered successfully: {}", user.getLogin());
        } catch (Exception e) {
            log.error("User registered error: {}", e.getMessage());
        }
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
     * Получение пользователя по ID
     *
     * @param id уникальный идентификатор
     * @return пользователь
     */
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
