package com.wayz.service.impl;

import com.wayz.model.User;
import com.wayz.repository.UserRepository;
import com.wayz.service.UserService;
import com.wayz.service.UserUpdateService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@Data
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Getter
    @Value("${jwt.secret}")
    private String secretKey;

    private final UserRepository userRepository;

    private final UserUpdateService userUpdateServiceImpl;

    public UserServiceImpl(UserRepository userRepository, UserUpdateService userUpdateServiceImpl) {
        this.userRepository = userRepository;
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
     * Получение пользователя по ID
     *
     * @param id уникальный идентификатор
     * @return пользователь
     */
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Получение пользователя по логину из БД
     *
     * @param login логин для поиска
     * @return объект пользователя найденный из БД
     */
    @Override
    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login).orElse(null);
    }

}
