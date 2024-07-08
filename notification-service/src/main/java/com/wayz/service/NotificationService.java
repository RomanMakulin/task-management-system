package com.wayz.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.wayz.dto.Notification;
import org.apache.commons.lang3.StringEscapeUtils;
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
            // Логирование исходного сообщения для отладки
            log.info("Полученное JSON сообщение: {}", message);

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

            // Преобразуем исправленную строку в объект Notification
            Notification notification = mapper.readValue(decodedMessage, Notification.class);

            // Отправляем уведомление по email
            mailService.sendMailNotification(notification);
        } catch (Exception e) {
            log.info("Ошибка получения данных из order-service: {}", e);
        }
    }


}
