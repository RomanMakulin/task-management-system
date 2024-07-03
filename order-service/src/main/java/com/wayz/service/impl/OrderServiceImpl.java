package com.wayz.service.impl;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.wayz.dto.Notification;
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

    private final JsonMapper mapper;

    private static final String TOPIC = "order-topic";

    public OrderServiceImpl(OrderRepository orderRepository,
                            UserServiceClientImpl userServiceClientImpl,
                            KafkaTemplate<String, String> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.userServiceClientImpl = userServiceClientImpl;
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = new JsonMapper();
    }

    /**
     * Отправка order-topic в сервис нотификаций
     *
     * @param order order или любое другое сообщение для сервиса нотификаций
     */
    public void sendOrder(Order order) {
        Notification notification = new Notification();

        try {
            User user = userServiceClientImpl.getUser(order.getUserId());

            Optional.ofNullable(user.getEmail()).ifPresent(notification::setEmail);
            Optional.ofNullable(user.getFirstName()).ifPresent(notification::setFirstName);
            Optional.ofNullable(user.getLastName()).ifPresent(notification::setLastName);
            Optional.ofNullable(user.getPhone()).ifPresent(notification::setPhone);
            Optional.ofNullable(user.getLogin()).ifPresent(notification::setLogin);

            notification.setOrder(order);

            String orderJson = mapper.writeValueAsString(notification);
            kafkaTemplate.send(TOPIC, mapper.writeValueAsString(orderJson));
            log.info("Информация о заказе {} отправлена в notification-service", notification);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка отправки объекта заказа в сервис нотификаций.");
        }
    }

    /**
     * Создание заказа с проверкой существует ли такой пользователь в системе
     *
     * @param orderDetails данные заказа
     * @return созданный заказ
     */
    @Override
    public Order createOrder(Order orderDetails) {
        log.info("Order create with orderDetails: {}" + orderDetails);
        try {
            if (userServiceClientImpl.getUser(orderDetails.getUserId()) != null) {
                Order newOrder = new Order();
                newOrder.setOrderDate(ZonedDateTime.now());
                newOrder.setStatus(OrderStatus.CREATED);
                newOrder.setOrderAddress(orderDetails.getOrderAddress());
                newOrder.setItems(orderDetails.getItems());
                newOrder.setUserId(orderDetails.getUserId());

                sendOrder(orderDetails);
                orderRepository.save(newOrder);
                return newOrder;
            } else throw new NullPointerException("User not found. Please try again to create order.");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Обновление заказа
     *
     * @param orderDetails данные для обновления заказа
     * @return обновленный заказ
     */
    @Override
    public Order updateOrder(Order orderDetails) {
        try {
            Optional<Order> orderToUpdate = orderRepository.findById(orderDetails.getID());
            if (orderToUpdate.isPresent()) {
                orderDetails.setStatus(OrderStatus.UPDATED);
                orderRepository.save(orderDetails);
                sendOrder(orderDetails);
                return orderDetails;
            } else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order ID must not be null");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Получение заказа по уникальному идентификатору
     *
     * @param id уникальный идентификатор заказа
     * @return объект заказа
     */
    @Override
    public Order getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

}
