package com.wayz.service.impl;

import com.wayz.model.Order;
import com.wayz.service.UserOrderService;
import com.wayz.service.integration.OrderClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserOrderServiceImpl implements UserOrderService {

    private final OrderClient orderClient;

    public UserOrderServiceImpl(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    /**
     * Получить список заказов у текущего пользователя
     * Запрос отправляется через orderClient, делающий запрос в сервис заказов
     *
     * @param token токен авторизации
     * @return список заказов текущего пользователя
     */
    @Override
    public List<Order> getCurrentUserOrderList(String token){
        return orderClient.getOrderListByCurrentUser(token);
    }

}
