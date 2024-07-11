package com.wayz.service.impl;

import com.wayz.dto.User;
import com.wayz.service.UserServiceClient;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Data
public class UserServiceClientImpl implements UserServiceClient {

    private final Logger log = LoggerFactory.getLogger(UserServiceClientImpl.class);

    private final RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl;

    public UserServiceClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public User getUserById(Long userId) {
        String url = constructUrl("getUserById", userId.toString());
        return sendRequest(url);
    }

    @Override
    public User getUserByLogin(String userLogin) {
        String url = constructUrl("getUserByLogin", userLogin);
        return sendRequest(url);
    }

    private String constructUrl(String endpoint, String parameter) {
        return String.format("%s/%s/%s", userServiceUrl, endpoint, parameter);
    }

    private User sendRequest(String url) {
        try {
            ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);
            log.info("Запрос в user-service успешно отправлен. Тело ответа: {}", response.getBody());
            return handleResponse(response);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении пользователя: " + e.getMessage(), e);
        }
    }

    private User handleResponse(ResponseEntity<User> response) {
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            log.error("Не удалось получить корректный ответ. Код состояния: {}", response.getStatusCode());
            throw new RuntimeException("Не удалось получить корректный ответ от user-service");
        }
    }
}
