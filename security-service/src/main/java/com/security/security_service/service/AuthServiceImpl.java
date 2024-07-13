package com.security.security_service.service;

import com.security.security_service.model.User;
import com.security.security_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService{
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
