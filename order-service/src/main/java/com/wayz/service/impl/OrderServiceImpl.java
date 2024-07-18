package com.wayz.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wayz.dto.*;
import com.wayz.model.Order;
import com.wayz.model.OrderStatus;
import com.wayz.model.submodels.OrderItem;
import com.wayz.repository.OrderRepository;
import com.wayz.service.OrderService;
import com.wayz.service.UserServiceClient;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис с логикой управления заказами
 */
@Service
@Data
public class OrderServiceImpl implements OrderService {

    /**
     * Логгер
     */
    private final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    /**
     * Репозиторий заказов
     */
    private final OrderRepository orderRepository;

    /**
     * Сервис клиента работы с User-service (интеграция)
     */
    private final UserServiceClient userServiceClient;

    /**
     * Kafka template для передачи сообщений через топики
     */
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Маппер для обработки JSON
     */
    private final ObjectMapper mapper;

    /**
     * Топик для передачи заказа в сервис нотификаций
     */
    private static final String TOPIC = "order-topic";

    public OrderServiceImpl(OrderRepository orderRepository,
                            UserServiceClientImpl userServiceClient,
                            KafkaTemplate<String, String> kafkaTemplate, ObjectMapper mapper) {
        this.orderRepository = orderRepository;
        this.userServiceClient = userServiceClient;
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

        User user = userServiceClient.getUserByLogin(createOrderDto.getLogin(), token);
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

        Order orderToUpdate = findOrderByIdFromDto(orderDetails.getId());
        User user = userServiceClient.getUserByLogin(orderDetails.getLogin(), token);

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
     * @param id order ID
     * @return объект заказа если найдет
     */
    public Order findOrderByIdFromDto(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found for given ID"));
    }

    /**
     * Получение заказа по уникальному идентификатору
     *
     * @param id    уникальный идентификатор заказа
     * @param token токен
     * @return объект заказа
     */
    @Override
    public Order getOrderById(Long id, String token) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    /**
     * Добавить новый товар в существующий заказ
     *
     * @param orderDetails данные из dto для добавления нового товара в заказ
     * @return статус выполнения запроса
     */
    @Override
    public ResponseEntity<String> addItemInOrder(AddItemInOrderDto orderDetails) {
        try {
            Order order = findOrderByIdFromDto(orderDetails.getOrderId());
            order.addItem(orderDetails.getOrderItem());
            orderRepository.save(order);
            return ResponseEntity.ok("Order's item added: " + order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    /**
     * Удаление конкретного товара по его имени в заказе
     *
     * @param orderId  идентификатор заказа
     * @param itemName имя товара
     * @return статус выполнения запроса
     */
    @Override
    public ResponseEntity<String> deleteItemFromOrder(Long orderId, String itemName) {
        try {
            Order order = findOrderByIdFromDto(orderId);
            Optional<OrderItem> orderItem = findItemByName(order, itemName);

            if (orderItem.isPresent()) {
                order.getItems().remove(orderItem.get());
                orderRepository.save(order);
                return ResponseEntity.ok("Order's item deleted: " + order);
            }

            // TODO перенести выше
            if (order.getItems().isEmpty()) {
                orderRepository.delete(order);
                return ResponseEntity.ok("Заказ удален так как в нем закончились товары.");
            }

            return ResponseEntity.badRequest().body("Item not found. Name: " + itemName);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Поиск товара по названию в заказе
     *
     * @param order    нужный заказ
     * @param itemName название товара
     * @return необходимый товар если есть
     */
    public Optional<OrderItem> findItemByName(Order order, String itemName) {
        return order.getItems().stream()
                .filter(item -> item.getName().equals(itemName)).findFirst();
    }

}
