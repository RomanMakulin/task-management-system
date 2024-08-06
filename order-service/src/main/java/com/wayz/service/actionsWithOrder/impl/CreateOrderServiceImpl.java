package com.wayz.service.actionsWithOrder.impl;

import com.wayz.dto.CreateOrderDto;
import com.wayz.dto.User;
import com.wayz.model.Order;
import com.wayz.model.submodels.OrderStatus;
import com.wayz.repository.OrderRepository;
import com.wayz.service.AbstractOrderService;
import com.wayz.service.OrderService;
import com.wayz.service.notify.NotificationService;
import com.wayz.service.history.OrderHistoryService;
import com.wayz.service.clientUser.UserServiceClient;
import com.wayz.service.actionsWithOrder.CreateOrderService;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

/**
 * Сервис создания заказов
 */
@Service
public class CreateOrderServiceImpl extends AbstractOrderService implements CreateOrderService {

    protected CreateOrderServiceImpl(UserServiceClient userServiceClient,
                                     NotificationService notificationService,
                                     OrderHistoryService orderHistoryService,
                                     @Lazy OrderService orderService) {
        super(null, userServiceClient, notificationService, orderHistoryService, orderService);
    }

    /**
     * Создание заказа с проверкой существует ли такой пользователь в системе
     *
     * @param createOrderDto данные заказа
     * @param token          токен авторизации
     * @return созданный заказ
     */
    @Override
    public ResponseEntity<Order> createOrder(CreateOrderDto createOrderDto, String token) {
        validateOrder(createOrderDto);

        User user = userServiceClient.getUserByLogin(createOrderDto.getLogin(), token);
        Order newOrder = buildOrder(createOrderDto, user);

        orderHistoryService.updateOrderHistory(newOrder, null);
        notificationService.orderNotifyKafka(newOrder, user);

        return ResponseEntity.ok(newOrder);
    }

    /**
     * Проверка валидности полей из запроса на создание заказа
     *
     * @param createOrderDto данные запроса
     */
    public void validateOrder(CreateOrderDto createOrderDto) {
        if (createOrderDto.getLogin() == null || createOrderDto.getLogin().isEmpty()) {
            throw new IllegalArgumentException("Ошибка создания заказа. Не передан login пользователя");
        }

        if (createOrderDto.getOrderAddress() == null || createOrderDto.getItems() == null || createOrderDto.getItems().isEmpty()) {
            throw new IllegalArgumentException("Ошибка создания заказа. Не переданы обязательные параметры для формирования заказа");
        }
    }

    /**
     * Билдер создания заказа по переданным данным из Dto
     *
     * @param createOrderDto данные из запроса о создании заказа
     * @param user           пользователь данного заказа
     * @return созданный объект заказа
     */
    public Order buildOrder(CreateOrderDto createOrderDto, User user) {
        Order newOrder = new Order();
        newOrder.setUserId(user.getID());
        newOrder.setOrderDate(ZonedDateTime.now());
        newOrder.setStatus(OrderStatus.CREATED);
        newOrder.setOrderAddress(createOrderDto.getOrderAddress());
        newOrder.setItems(createOrderDto.getItems());
        return newOrder;
    }
}
