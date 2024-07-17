package com.wayz.controller;

import com.wayz.dto.CreateOrderDto;
import com.wayz.dto.UpdateOrderDto;
import com.wayz.model.Order;
import com.wayz.service.OrderService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Общий контроллер управления заказами
 */
@RestController
@RequestMapping("/orders")
public class Controller {

    /**
     * Сервис работы (логики) с заказами
     */
    private final OrderService orderService;

    public Controller(OrderService orderService, OrderService orderService1) {
        this.orderService = orderService1;
    }

    /**
     * Создание нового заказа
     *
     * @param order детали заказа
     * @return ответ о статусе создания заказа, содержащий заказ
     */
    @PostMapping("/create")
    public ResponseEntity<Order> create(@RequestBody CreateOrderDto order,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.ok(orderService.createOrder(order, token));
    }

    /**
     * Обновление текущего заказа
     *
     * @param order тело запроса с новыми данными о заказе
     * @param token токен авторизации запроса
     * @return статус ответа с обновленным заказом
     */
    @PostMapping("/update")
    public ResponseEntity<Order> update(@RequestBody UpdateOrderDto order,
                                        @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return ResponseEntity.ok(orderService.updateOrder(order, token));
    }

}
