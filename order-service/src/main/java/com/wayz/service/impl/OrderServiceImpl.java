package com.wayz.service.impl;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.wayz.model.Order;
import com.wayz.model.OrderStatus;
import com.wayz.repository.OrderRepository;
import com.wayz.service.OrderService;
import com.wayz.service.UserServiceClient;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@Data
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final UserServiceClient userServiceClient;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "order-topic";

    public OrderServiceImpl(OrderRepository orderRepository, UserServiceClient userServiceClient, KafkaTemplate<String, String> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.userServiceClient = userServiceClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Отправка order-topic в сервис нотификаций
     *
     * @param message order или любое другое сообщение для сервиса нотификаций
     */
    public void sendOrder(String message) {
        kafkaTemplate.send(TOPIC, message);
    }

    /**
     * Создание заказа с проверкой существует ли такой пользователь в системе
     *
     * @param orderDetails данные заказа
     * @return созданный заказ
     */
    @Override
    public Order createOrder(Order orderDetails) {
        try {
            if (userServiceClient.getUser(orderDetails.getUserId()) != null) {
                Order newOrder = new Order();
                newOrder.setOrderDate(ZonedDateTime.now());
                newOrder.setOrderStatus(OrderStatus.CREATED);
                newOrder.setOrderAddress(orderDetails.getOrderAddress());
                newOrder.setItems(orderDetails.getItems());
                newOrder.setUserId(orderDetails.getUserId());

                JsonMapper mapper = new JsonMapper();
                String orderJson = mapper.writeValueAsString(newOrder);
                sendOrder(orderJson);

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
            if (orderToUpdate.isPresent()) return orderRepository.save(orderDetails);
            else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order ID must not be null");
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
