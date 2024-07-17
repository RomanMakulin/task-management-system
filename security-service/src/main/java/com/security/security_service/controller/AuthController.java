package com.security.security_service.controller;

import com.security.security_service.dto.AuthRequest;
import com.security.security_service.model.User;
import com.security.security_service.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    /**
     * Основная логика авторизации в сервисе авторизации
     */
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Запрос на регистрацию
     *
     * @param user тело запроса, содержащее информацию о пользователе для регистрации
     * @return статус ответа при удачной регистрации
     */
    @PostMapping("/registration")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        return ResponseEntity.ok("User registered: " + authService.registerUser(user));
    }

    /**
     * Запрос на получение токена (логирование) для авторизации последующих защищенных запросов
     *
     * @param authRequest тело запроса с информацией для авторизации: логин и пароль
     * @return токен
     */
    @PostMapping("/token")
    public ResponseEntity<String> getToken(@RequestBody AuthRequest authRequest) {
        return authService.loginUser(authRequest);
    }

    /**
     * Валидация токена (проверка действительности)
     *
     * @param token проверяемый токен в url запросе
     * @return статус и сообщение о рабочем токене если это так
     */
    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
        authService.validateToken(token);
        return ResponseEntity.ok("Токен прошел проверку и действителен!");
    }
}
