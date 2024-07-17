package com.wayz.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wayz.dto.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final ObjectMapper mapper;

    /**
     * Интерфейс отправки данных на почту пользователей
     */
    private final MailService mailService;

    public NotificationService(ObjectMapper mapper, MailService mailService) {
        this.mapper = mapper;
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
            Notification notification = mapper.readValue(decodedMessage(message), Notification.class);
            mailService.sendMailNotification(notification);
        } catch (Exception e) {
            log.info("Ошибка получения данных из order-service: {}", e);
        }
    }

    /**
     * Декодирует полученное сообщение в нужный формат JSON
     *
     * @param message полученное сообщение из kafka
     * @return декодированное сообщение
     */
    public String decodedMessage(String message) {
        // Убираем все обратные слэши и лишние кавычки
        String decodedMessage = message.replace("\\\"", "\"")
                .replace("\\\\", "\\")
                .replace("\"{", "{")
                .replace("}\"", "}")
                .replace("\\", "");

        // Убираем лишние кавычки в начале и в конце строки
        if (decodedMessage.startsWith("\"") && decodedMessage.endsWith("\"")) {
            decodedMessage = decodedMessage.substring(1, decodedMessage.length() - 1);
        }
        return decodedMessage;
    }

}
