package com.wayz.service.impl;

import com.wayz.dto.User;
import com.wayz.service.UserServiceClient;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
    public User getUser(Long userId) {
        String url = userServiceUrl + "getUserById/" + userId;
        log.info(url);
        try {
            ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);
            log.info("Запрос в user-service успешно отправлен.\nТело ответа: {}", response.getBody());
            return response.getBody();
        } catch (Exception e) {
            log.info("Error getting user: " + e.getMessage());
            throw new RuntimeException("Error getting user: " + e.getMessage());
        }
    }


}
