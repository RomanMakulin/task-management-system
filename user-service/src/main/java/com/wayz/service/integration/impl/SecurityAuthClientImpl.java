package com.wayz.service.integration.impl;

import com.wayz.model.Order;
import com.wayz.model.User;
import com.wayz.service.integration.SecurityAuthClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class SecurityAuthClientImpl extends AbstractIntegrationService<User> implements SecurityAuthClient {

    /**
     * URL до security-service
     */
    @Value("${security.service.url}")
    private String securityServiceUrl;


    protected SecurityAuthClientImpl(RestTemplate restTemplate) {
        super(restTemplate);
    }

    /**
     * Отправка GET запроса и получение ответа от security-service
     *
     * @param token токен авторизации
     * @return список заказов текущего пользователя
     */
    @Override
    public User getCurrentAuthUser(String token) {
        String url = constructUrl(securityServiceUrl, "get-auth-user", null);
        return sendRequest(url, token, new ParameterizedTypeReference<User>() {}, HttpMethod.GET, null);
    }

}
