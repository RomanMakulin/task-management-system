package com.wayz.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wayz.dto.*;
import com.wayz.model.Order;
import com.wayz.model.OrderStatus;
import com.wayz.model.submodels.OrderItem;
import com.wayz.repository.OrderRepository;
import com.wayz.service.CreateOrderService;
import com.wayz.service.NotificationService;
import com.wayz.service.OrderService;
import com.wayz.service.UserServiceClient;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Сервис с логикой управления заказами
 */
@Service
public class OrderServiceImpl implements OrderService {

    /**
     * Репозиторий заказов
     */
    private final OrderRepository orderRepository;

    /**
     * Сервис клиента работы с User-service (интеграция)
     */
    private final UserServiceClient userServiceClient;

    /**
     * Сервис отправки нотификаций через Kafka
     */
    private final NotificationService notificationService;

    /**
     * Сервис создания заказов
     */
    private final CreateOrderService createOrderService;

    public OrderServiceImpl(OrderRepository orderRepository, UserServiceClient userServiceClient, NotificationService notificationService, CreateOrderService createOrderService) {
        this.orderRepository = orderRepository;
        this.userServiceClient = userServiceClient;
        this.notificationService = notificationService;
        this.createOrderService = createOrderService;
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
        return createOrderService.createOrder(createOrderDto, token);
    }

    /**
     * Обновление заказа
     *
     * @param orderDetails данные для обновления заказа
     * @return обновленный заказ
     */
    @Override
    public ResponseEntity<Order> updateOrder(UpdateOrderDto orderDetails, String token) {
        Order orderToUpdate = findOrderByIdFromDto(orderDetails.getId());
        User user = userServiceClient.getUserByLogin(orderDetails.getLogin(), token);

        orderToUpdate.setStatus(OrderStatus.UPDATED);
        Optional.ofNullable(orderDetails.getOrderAddress()).ifPresent(orderToUpdate::setOrderAddress);
        Optional.ofNullable(orderDetails.getItems()).ifPresent(orderToUpdate::setItems);

        notificationService.orderNotifyKafka(orderToUpdate, user);
        orderRepository.save(orderToUpdate);
        return ResponseEntity.ok(orderToUpdate);
    }

    /**
     * Поиск заказа по id
     *
     * @param id order ID
     * @return объект заказа если найдет
     */
    @Override
    public Order findOrderByIdFromDto(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found for given ID"));
    }

    /**
     * Изменить владельца заказа
     *
     * @param orderId  идентификатор заказа
     * @param username логин нового владельца заказа
     * @param token    токен авторизации
     * @return статус обновления заказа
     */
    @Override
    public ResponseEntity<Order> changeOwner(Long orderId, String username, String token) {
        Order order = findOrderByIdFromDto(orderId);
        User user = userServiceClient.getUserByLogin(username, token);
        order.setUserId(user.getID());
        orderRepository.save(order);
        return ResponseEntity.ok(order);
    }

    /**
     * Показать список всех заказов пользователя
     *
     * @param username логин пользователя
     * @param token    токен авторизации запроса
     * @return статус выполнения - лист заказов
     */
    @Override
    public ResponseEntity<List<Order>> getUserOrders(String username, String token) {
        User user = userServiceClient.getUserByLogin(username, token);
        return ResponseEntity.ok(orderRepository.findAll().stream()
                .filter(i -> i.getUserId().equals(user.getID())).toList());
    }
}
