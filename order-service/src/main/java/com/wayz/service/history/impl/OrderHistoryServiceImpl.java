package com.wayz.service.history.impl;

import com.wayz.model.Order;
import com.wayz.model.OrderHistory;
import com.wayz.model.submodels.OrderStatus;
import com.wayz.repository.OrderHistoryRepository;
import com.wayz.repository.OrderRepository;
import com.wayz.service.OrderService;
import com.wayz.service.history.OrderHistoryService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public OrderHistoryServiceImpl(OrderRepository orderRepository,
                                   @Lazy OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    /**
     * Добавление новой записи истории к заказу
     *
     * @param order     текущий заказ
     * @param newStatus новый статус заказа
     */
    @Override
    public void updateOrderHistory(Order order, OrderStatus newStatus) {
        OrderHistory orderHistory = new OrderHistory(order, newStatus);
        order.addHistory(orderHistory);
        order.setStatus(newStatus);
        orderRepository.save(order);
    }

    /**
     * Получить историю (список) изменения заказа
     *
     * @param orderId идентификатор заказа
     * @return список истории заказов
     */
    @Override
    public ResponseEntity<List<OrderHistory>> getOrderHistoryByOrderId(Long orderId) {
        Order order = orderService.findOrderById(orderId);
        return ResponseEntity.ok(order.getOrderHistoryList());
    }


}
