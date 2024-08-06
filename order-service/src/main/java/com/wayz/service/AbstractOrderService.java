package com.wayz.service;

import com.wayz.repository.OrderRepository;
import com.wayz.service.clientUser.UserServiceClient;
import com.wayz.service.history.OrderHistoryService;
import com.wayz.service.notify.NotificationService;
import org.springframework.context.annotation.Lazy;

/**
 * Общая точка доступа управления ключевыми зависимостями
 */
public abstract class AbstractOrderService {

    protected final OrderRepository orderRepository;
    protected final UserServiceClient userServiceClient;
    protected final NotificationService notificationService;
    protected final OrderHistoryService orderHistoryService;
    protected final OrderService orderService;

    protected AbstractOrderService(OrderRepository orderRepository,
                                   UserServiceClient userServiceClient,
                                   NotificationService notificationService,
                                   OrderHistoryService orderHistoryService,
                                   @Lazy OrderService orderService) {
        this.orderRepository = orderRepository;
        this.userServiceClient = userServiceClient;
        this.notificationService = notificationService;
        this.orderHistoryService = orderHistoryService;
        this.orderService = orderService;
    }

}
