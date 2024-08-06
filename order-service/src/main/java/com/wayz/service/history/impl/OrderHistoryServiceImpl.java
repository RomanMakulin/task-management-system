package com.wayz.service.history.impl;

import com.wayz.model.Order;
import com.wayz.model.OrderHistory;
import com.wayz.model.submodels.OrderStatus;
import com.wayz.repository.OrderRepository;
import com.wayz.service.history.OrderHistoryService;
import org.springframework.stereotype.Service;

@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {

    private final OrderRepository orderRepository;

    public OrderHistoryServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
}
