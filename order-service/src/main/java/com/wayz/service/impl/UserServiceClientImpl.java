package com.wayz.service.impl;

import com.wayz.dto.User;
import com.wayz.exceptions.UserNotFoundException;
import com.wayz.service.UserServiceClient;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Сервис-клиент для работы с user-service
 */
@Service
@Data
public class UserServiceClientImpl implements UserServiceClient {

    /**
     * Логгер
     */
    private final Logger log = LoggerFactory.getLogger(UserServiceClientImpl.class);

    /**
     * Взаимодействие с user-service через rest протокол
     */
    private final RestTemplate restTemplate;

    /**
     * URL до сервиса User-service
     */
    @Value("${user.service.url}")
    private String userServiceUrl;

    public UserServiceClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Получение пользователя из User-service по ID
     *
     * @param userId user id
     * @param token  токен авторизации запроса
     * @return найденный пользователь
     */
    @Override
    public User getUserById(Long userId, String token) {
        String url = constructUrl("getUserById", userId.toString());
        return sendRequest(url, token);
    }

    /**
     * Получение пользователя из User-service по логину
     *
     * @param userLogin логин пользователя
     * @param token     токен авторизации запроса
     * @return найденный пользователь
     */
    @Override
    public User getUserByLogin(String userLogin, String token) {
        String url = constructUrl("getUserByLogin", userLogin);
        return sendRequest(url, token);
    }

    /**
     * Формирование ссылки для запроса в user-service
     *
     * @param endpoint  энд-поинт для обращения в user-service
     * @param parameter параметр по которому нужно искать
     * @return сформированная ссылка для запроса
     */
    private String constructUrl(String endpoint, String parameter) {
        return String.format("%s/%s/%s", userServiceUrl, endpoint, parameter);
    }

    /**
     * Отправка запроса
     *
     * @param url   url запроса
     * @param token токен авторизации запроса
     * @return найденный пользователь
     */
    private User sendRequest(String url, String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, token); // Добавляем токен в заголовок
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, User.class);
            log.info("Запрос в user-service успешно отправлен. Тело ответа: {}", response.getBody());
            return handleResponse(response);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getStatusCode(), e.getMessage());
        }
    }

    /**
     * Доп. обработчик запроса
     *
     * @param response запрос в User-service
     * @return найденный пользователь или же ошибка в случае неудачи
     */
    private User handleResponse(ResponseEntity<User> response) {
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            log.error("Не удалось получить корректный ответ. Код состояния: {}", response.getStatusCode());
            throw new RuntimeException("Не удалось получить корректный ответ от user-service");
        }
    }
}
