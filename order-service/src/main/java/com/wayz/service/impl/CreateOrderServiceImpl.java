package com.wayz.service.impl;

import com.wayz.dto.CreateOrderDto;
import com.wayz.dto.User;
import com.wayz.model.Order;
import com.wayz.model.submodels.OrderStatus;
import com.wayz.repository.OrderRepository;
import com.wayz.service.CreateOrderService;
import com.wayz.service.NotificationService;
import com.wayz.service.UserServiceClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

/**
 * Сервис создания заказов
 */
@Service
public class CreateOrderServiceImpl implements CreateOrderService {

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
     * Конструктор класса
     */
    public CreateOrderServiceImpl(OrderRepository orderRepository, UserServiceClient userServiceClient, NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.userServiceClient = userServiceClient;
        this.notificationService = notificationService;
    }

    /**
     * Создание заказа с проверкой существует ли такой пользователь в системе
     *
     * @param createOrderDto данные заказа
     * @param token токен авторизации
     * @return созданный заказ
     */
    @Override
    public ResponseEntity<Order> createOrder(CreateOrderDto createOrderDto, String token) {
        validateOrder(createOrderDto);

        User user = userServiceClient.getUserByLogin(createOrderDto.getLogin(), token);
        Order newOrder = buildOrder(createOrderDto, user);

        orderRepository.save(newOrder);
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
