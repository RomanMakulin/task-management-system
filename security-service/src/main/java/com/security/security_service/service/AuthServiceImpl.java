package com.security.security_service.service;

import com.security.security_service.config.CustomUserDetails;
import com.security.security_service.dto.AuthRequest;
import com.security.security_service.model.User;
import com.security.security_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис авторизации пользователей
 */
@Service
public class AuthServiceImpl implements AuthService {

    /**
     * Логгер
     */
    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Метод регистрации пользователей в системе
     *
     * @param user данные для регистрации пользователя
     * @return сохраненный пользователь в БД
     */
    @Override
    public User registerUser(User user) {
        if (userRepository.existsByLogin(user.getLogin())) {
            throw new IllegalArgumentException("Login already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info("User registered successfully: {}", user.getLogin());
        return user;
    }

    /**
     * Логика входа в систему
     *
     * @param authRequest данные о пользователе, содержащие логин и пароль
     * @return статус выполнения логирования
     */
    @Override
    public ResponseEntity<String> loginUser(AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
            return ResponseEntity.ok(generateToken(authRequest.getLogin()));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Invalid login or password");
        }
    }

    /**
     * Генерация токена
     *
     * @param login логин пользователя
     * @return токен
     */
    public String generateToken(String login) {
        return jwtService.generateToken(login);
    }

    /**
     * Проверка токена на валидность
     *
     * @param token токен для проверки
     */
    @Override
    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

    /**
     * Получить текущего авторизованного пользователя
     *
     * @return объект пользователя
     */
    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userRepository.findByLogin(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userDetails.getUsername()));
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

}
