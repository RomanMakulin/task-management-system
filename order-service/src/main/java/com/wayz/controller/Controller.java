package com.wayz.controller;

import com.wayz.model.Order;
import com.wayz.service.OrderService;
import com.wayz.service.UserServiceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("/create")
    public ResponseEntity<Order> create(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

}
