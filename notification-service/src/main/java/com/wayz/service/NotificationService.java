package com.wayz.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @KafkaListener(topics = "task_topic", groupId = "group_id")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
        // Логика отправки уведомлений
    }

}
