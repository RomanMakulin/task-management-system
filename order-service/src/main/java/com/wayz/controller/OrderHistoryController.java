package com.wayz.controller;

import com.wayz.model.OrderHistory;
import com.wayz.service.history.OrderHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderHistoryController {

    private final OrderHistoryService orderHistoryService;

    public OrderHistoryController(OrderHistoryService orderHistoryService) {
        this.orderHistoryService = orderHistoryService;
    }

    /**
     * Получить историю (список) изменения заказа
     *
     * @param orderId идентификатор заказа
     * @return список истории заказов
     */
    @GetMapping("/get-history/{id}")
    public ResponseEntity<List<OrderHistory>> getOrderHistoryList(@PathVariable("id") Long orderId){
        return orderHistoryService.getOrderHistoryByOrderId(orderId);
    }

}
