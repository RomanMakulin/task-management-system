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
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            userRepository.save(user);
            log.info("User registered successfully: {}", user.getLogin());
        } catch (Exception e) {
            log.error("User registered error: {}", e.getMessage());
        }
    }

    /**
     * Логика входа пользователя в систему с проверкой пароля и установкой куки хранения JWT токена
     *
     * @param user данные авторизуемого пользователя
     * @return статус выполнения запроса
     */
    @Override
    public ResponseEntity<String> loginUser(User user) {
        if (!checkPasswordForLogin(user)) {
            return ResponseEntity.status(403).body("Incorrect password");
        }
        return ResponseEntity.ok("Login successful. Login: " + user.getLogin());
    }

    /**
     * Проверка пароля для авторизации пользователя
     *
     * @param user данные авторизуемого пользователя
     * @return логическое значение
     */
    public boolean checkPasswordForLogin(User user) {
        Optional<User> findUser = userRepository.findByLogin(user.getLogin());
        return findUser.filter(value -> user.getLogin().equals(value.getLogin())).isPresent();
    }

}
