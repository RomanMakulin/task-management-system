package com.wayz.service;

import lombok.Data;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Data
public class OrderService {

    private static final String TOPIC = "order-topic";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderService(KafkaTemplate<String, String> kafkaTemplate) {
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

}
