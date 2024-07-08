package com.wayz.service;

import com.wayz.dto.AuthDdo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Getter
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${user.service.url}")
    private String userServiceUrl;

    private final RestTemplate restTemplate;

    public AuthServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    /**
     * Проверка пароля пользователя
     *
     * @param currentUserDetails данные пользователя, пытающегося войти в систему
     * @return логический ответ на правильность пароля
     */
    @Override
    public boolean checkPassword(AuthDdo currentUserDetails) {
        AuthDdo userFromDatabase = getUserByLogin(currentUserDetails);
        return userFromDatabase.getPassword().equals(currentUserDetails.getPassword());
    }

    /**
     * Устанавливаем куки для автоматизации токенов
     * @param authDdo
     * @param response
     */
    @Override
    public void setCookie(AuthDdo authDdo, HttpServletResponse response){
        String token = Jwts.builder()
                .setSubject(authDdo.getLogin())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        ResponseCookie cookie = ResponseCookie.from("JWT_TOKEN", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(1000 * 60 * 60 * 10)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * Получение пользователя запросом с БД по логину
     *
     * @param currentUserDetails данные пользователя, пытающегося войти в систему
     * @return пользователь из запроса
     */
    public AuthDdo getUserByLogin(AuthDdo currentUserDetails) {
        String url = userServiceUrl + "getUserByLogin/" + currentUserDetails.getLogin();
        log.info(url);
        try {
            ResponseEntity<AuthDdo> response = restTemplate.getForEntity(url, AuthDdo.class);
            log.info("Запрос в user-service успешно отправлен.\nТело ответа: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            log.info("Error getting user: " + e.getMessage());
            throw new RuntimeException("Error getting user: " + e.getMessage());
        }
    }


}
