package com.wayz.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wayz.dto.Notification;
import com.wayz.dto.User;
import com.wayz.model.Order;
import com.wayz.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис отправки нотификаций через kafka
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    /**
     * Логгер
     */
    private final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    /**
     * Топик для передачи заказа в сервис нотификаций
     */
    private static final String TOPIC = "order-topic";

    /**
     * Kafka template для передачи сообщений через топики
     */
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Маппер для обработки JSON
     */
    private final ObjectMapper mapper;

    public NotificationServiceImpl(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper mapper) {
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
}
