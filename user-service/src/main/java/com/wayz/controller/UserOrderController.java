package com.wayz.controller;

import com.wayz.model.Order;
import com.wayz.service.UserOrderService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер управления заказами текущего пользователя
 */
@RestController
@RequestMapping("/users/order")
public class UserOrderController {

    /**
     * Сервис управления заказами пользователя
     */
    private final UserOrderService userOrderService;

    public UserOrderController(UserOrderService userOrderService) {
        this.userOrderService = userOrderService;
    }

    /**
     * Получить список заказов у текущего пользователя
     *
     * @param token токен авторизации
     * @return список объектов заказа
     */
    @GetMapping("/list")
    public ResponseEntity<List<Order>> getUserOrderList(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.ok(userOrderService.getCurrentUserOrderList(token));
    }

}
