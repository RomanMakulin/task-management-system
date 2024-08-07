package com.wayz.service.integration.impl;

import com.wayz.model.Order;
import com.wayz.service.UserInfoService;
import com.wayz.service.integration.OrderClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Имплементация абстрактной интеграции с order-service
 */
@Service
public class OrderClientImpl extends AbstractIntegrationService<List<Order>> implements OrderClient {

    /**
     * URL до order-service
     */
    @Value("${order.service.url}")
    private String orderServiceUrl;

    private final UserInfoService userInfoService;

    protected OrderClientImpl(RestTemplate restTemplate, UserInfoService userInfoService) {
        super(restTemplate);
        this.userInfoService = userInfoService;
    }

    /**
     * Отправка GET запроса и получение списка заказов для авторизовааного пользователя от order-service
     * Сначала отправляется запрос в security service для получения авторизованного текущего пользователя (userInfoService)
     *
     * @param token токен авторизации
     * @return список заказов текущего пользователя
     */
    @Override
    public List<Order> getOrderListByCurrentUser(String token) {
        String username = userInfoService.getCurrentUser(token).getLogin();
        String url = constructUrl(orderServiceUrl, "get-all-user-orders", username);
        return sendRequest(url, token, new ParameterizedTypeReference<List<Order>>() {}, HttpMethod.GET, null);
    }

}
