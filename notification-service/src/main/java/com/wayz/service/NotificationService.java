package com.wayz.service;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.wayz.dto.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final JsonMapper mapper = new JsonMapper();

    /**
     * Интерфейс отправки данных на почту пользователей
     */
    private final MailService mailService;

    public NotificationService(MailService mailService) {
        this.mailService = mailService;
    }

    /**
     * Kafka слушатель топика order-topic из сервиса order-service
     * Получает тело - нотификацию и отправляет email сообщение пользователю в нотификации
     *
     * @param message полученный объект json в строчке из order-service
     */
    @KafkaListener(topics = "order-topic", groupId = "group_id")
    public void consume(String message) {
        try {
            Notification notification = mapper.readValue(message, Notification.class);
            mailService.sendMailNotification(notification);
        } catch (Exception e) {
            log.info("Ошибка получения данных из order-service: {}", message);
        }
    }


}
