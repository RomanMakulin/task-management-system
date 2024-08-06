package com.wayz.service.actionsWithOrder.impl;

import com.wayz.model.Order;
import com.wayz.model.submodels.OrderStatus;
import com.wayz.repository.OrderRepository;
import com.wayz.service.OrderService;
import com.wayz.service.actionsWithOrder.DeleteOrderService;
import com.wayz.service.history.OrderHistoryService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DeleteOrderServiceImpl implements DeleteOrderService {

    private final OrderRepository orderRepository;
    private final OrderHistoryService orderHistoryService;
    private final OrderService orderService;

    public DeleteOrderServiceImpl(OrderRepository orderRepository,
                                  OrderHistoryService orderHistoryService,
                                  @Lazy OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderHistoryService = orderHistoryService;
        this.orderService = orderService;
    }

    /**
     * Установить заказу статус "Удален"
     * При этом нет удаления заказа из базы данных
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    @Override
    public ResponseEntity<Order> deleteOrder(Long orderId) {
        Order order = orderService.findOrderById(orderId);
        orderHistoryService.updateOrderHistory(order, OrderStatus.DELETED);
        return ResponseEntity.ok(order);
    }

}
