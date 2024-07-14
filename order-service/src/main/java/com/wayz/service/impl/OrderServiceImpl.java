package com.wayz.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wayz.dto.Notification;
import com.wayz.dto.CreateOrderDto;
import com.wayz.dto.UpdateOrderDto;
import com.wayz.dto.User;
import com.wayz.model.Order;
import com.wayz.model.OrderStatus;
import com.wayz.repository.OrderRepository;
import com.wayz.service.OrderService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@Data
public class OrderServiceImpl implements OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;

    private final UserServiceClientImpl userServiceClientImpl;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper mapper;

    private static final String TOPIC = "order-topic";

    public OrderServiceImpl(OrderRepository orderRepository,
                            UserServiceClientImpl userServiceClientImpl,
                            KafkaTemplate<String, String> kafkaTemplate, ObjectMapper mapper) {
        this.orderRepository = orderRepository;
        this.userServiceClientImpl = userServiceClientImpl;
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
    }

    /**
     * Отправка order-topic в сервис нотификаций
     *
     * @param order order или любое другое сообщение для сервиса нотификаций
     */
    public void orderNotifyKafka(Order order, User user) {
        Notification notification = new Notification();

        try {
            Optional.ofNullable(user.getEmail()).ifPresent(notification::setEmail);
            Optional.ofNullable(user.getFirstName()).ifPresent(notification::setFirstName);
            Optional.ofNullable(user.getLastName()).ifPresent(notification::setLastName);
            Optional.ofNullable(user.getPhone()).ifPresent(notification::setPhone);
            Optional.ofNullable(user.getLogin()).ifPresent(notification::setLogin);

            notification.setOrder(order);

            String orderJson = mapper.writeValueAsString(notification);
            kafkaTemplate.send(TOPIC, mapper.writeValueAsString(orderJson));
            log.info("Информация о заказе {} отправлена в notification-service\nJSON: {}", notification, orderJson);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка отправки объекта заказа в сервис нотификаций.");
        }
    }

    /**
     * Создание заказа с проверкой существует ли такой пользователь в системе
     *
     * @param createOrderDto данные заказа
     * @param token
     * @return созданный заказ
     */
    @Override
    public Order createOrder(CreateOrderDto createOrderDto, String token) {
        validateOrder(createOrderDto);

        log.info("Создание заказа для пользователя: {}", createOrderDto.getLogin());

        User user = userServiceClientImpl.getUserByLogin(createOrderDto.getLogin(), token);
        Order newOrder = buildOrder(createOrderDto, user);

        orderRepository.save(newOrder);
        orderNotifyKafka(newOrder, user);
        log.info("Order created: {}", newOrder);
        return newOrder;
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

    /**
     * Обновление заказа
     *
     * @param orderDetails данные для обновления заказа
     * @return обновленный заказ
     */
    @Override
    public Order updateOrder(UpdateOrderDto orderDetails, String token) {

        Order orderToUpdate = findOrderById(orderDetails);
        User user = userServiceClientImpl.getUserByLogin(orderDetails.getLogin(), token);

        orderToUpdate.setStatus(OrderStatus.UPDATED);
        Optional.ofNullable(orderDetails.getOrderAddress()).ifPresent(orderToUpdate::setOrderAddress);
        Optional.ofNullable(orderDetails.getItems()).ifPresent(orderToUpdate::setItems);

        orderNotifyKafka(orderToUpdate, user);
        orderRepository.save(orderToUpdate);
        log.info("Order updated: {}", orderToUpdate);
        return orderToUpdate;
    }

    /**
     * Поиск заказа по id
     *
     * @param orderDetails данные из запроса
     * @return объект заказа если найдет
     */
    public Order findOrderById(UpdateOrderDto orderDetails) {
        return orderRepository.findById(orderDetails.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found for given ID"));
    }

    /**
     * Получение заказа по уникальному идентификатору
     *
     * @param id    уникальный идентификатор заказа
     * @param token
     * @return объект заказа
     */
    @Override
    public Order getOrderById(Long id, String token) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

}
