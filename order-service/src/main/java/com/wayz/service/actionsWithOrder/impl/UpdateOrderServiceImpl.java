package com.wayz.service.actionsWithOrder.impl;

import com.wayz.dto.UpdateOrderDto;
import com.wayz.dto.User;
import com.wayz.model.Order;
import com.wayz.model.submodels.OrderStatus;
import com.wayz.service.*;
import com.wayz.service.actionsWithOrder.UpdateOrderService;
import com.wayz.service.clientUser.UserServiceClient;
import com.wayz.service.history.OrderHistoryService;
import com.wayz.service.notify.NotificationService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateOrderServiceImpl extends AbstractOrderService implements UpdateOrderService {

    protected UpdateOrderServiceImpl(UserServiceClient userServiceClient,
                                     NotificationService notificationService,
                                     OrderHistoryService orderHistoryService,
                                     @Lazy OrderService orderService) {
        super(null, userServiceClient, notificationService, orderHistoryService, orderService);
    }

    /**
     * Обновление заказа
     *
     * @param orderDetails данные для обновления заказа
     * @return обновленный заказ
     */
    @Override
    public ResponseEntity<Order> updateOrder(UpdateOrderDto orderDetails, String token) {
        Order orderToUpdate = orderService.findOrderByIdFromDto(orderDetails.getId());
        User user = userServiceClient.getUserByLogin(orderDetails.getLogin(), token);

        updateOrderDetails(orderToUpdate, orderDetails);

        orderHistoryService.updateOrderHistory(orderToUpdate, OrderStatus.UPDATED); // history
        notificationService.orderNotifyKafka(orderToUpdate, user); // email notify
        return ResponseEntity.ok(orderToUpdate);
    }

    /**
     * Обновление заказа из DTO
     *
     * @param orderToUpdate объект заказа для обновления
     * @param orderDetails  данные о заказе, которые необходимо вставить в объект заказа
     */
    public void updateOrderDetails(Order orderToUpdate, UpdateOrderDto orderDetails) {
        Optional.ofNullable(orderDetails.getOrderAddress()).ifPresent(orderToUpdate::setOrderAddress);
        Optional.ofNullable(orderDetails.getItems()).ifPresent(orderToUpdate::setItems);
    }

}
