package com.wayz.controller;

import com.wayz.dto.CreateOrderDto;
import com.wayz.dto.UpdateOrderDto;
import com.wayz.model.Order;
import com.wayz.service.OrderService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Общий контроллер управления заказами
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    /**
     * Сервис работы (логики) с заказами
     */
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Создание нового заказа
     *
     * @param order детали заказа
     * @return ответ о статусе создания заказа, содержащий заказ
     */
    @PostMapping("/create")
    public ResponseEntity<Order> create(@RequestBody CreateOrderDto order, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return orderService.createOrder(order, token);
    }

    /**
     * Обновление текущего заказа
     *
     * @param order тело запроса с новыми данными о заказе
     * @param token токен авторизации запроса
     * @return статус ответа с обновленным заказом
     */
    @PostMapping("/update")
    public ResponseEntity<Order> update(@RequestBody UpdateOrderDto order, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return orderService.updateOrder(order, token);
    }

    /**
     * Изменить владельца заказа
     *
     * @param orderId  идентификатор заказа
     * @param needUserName логин нового владельца
     * @param token    токен авторизации запроса
     * @return статус выполнения - обновленный order
     */
    @PostMapping("/change-order-owner")
    public ResponseEntity<Order> changeOrderOwner(@RequestParam Long orderId,
                                                  @RequestParam String needUserName,
                                                  @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        return orderService.changeOwner(orderId, needUserName, token);
    }

    /**
     * Показать список всех заказов пользователя
     *
     * @param username логин пользователя
     * @param token    токен авторизации запроса
     * @return статус выполнения - лист заказов
     */
    @GetMapping("/get-all-user-orders/{username}")
    public ResponseEntity<List<Order>> getAllUserOrders(@PathVariable("username") String username, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return orderService.getUserOrders(username, token);
    }

}
