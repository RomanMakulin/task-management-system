package com.wayz.service;

import com.wayz.config.MailConfig;
import com.wayz.dto.Notification;
import com.wayz.dto.OrderItem;
import com.wayz.dto.OrderStatus;
import com.wayz.view.EmailMessage;
import lombok.Data;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Data
@Service
public class MailServiceImpl implements MailService {

    private final MailConfig mailConfig;

    private final EmailMessage emailMessage;

    public MailServiceImpl(MailConfig mailConfig, EmailMessage emailMessage) {
        this.mailConfig = mailConfig;
        this.emailMessage = emailMessage;
    }

    /**
     * Метод отправки полученной нотификации на почту пользователя
     *
     * @param notification полученная нотификация из сервиса order-service
     */
    @Override
    public void sendMailNotification(Notification notification) {
        String toUserEmail = notification.getEmail();
        String messageBody = emailMessage.create(notification);
        String emailTopic = "";

        switch (notification.getOrder().getStatus()) {
            case CREATED -> emailTopic = "Создан новый заказ!";
            case UPDATED -> emailTopic = "Ваш заказ обновлен!";
            case DELETED -> emailTopic = "Ваш заказ удален.";
            default -> throw new IllegalStateException("Unexpected value: " + notification.getOrder().getStatus());
        }
        mailConfig.messageManage(messageBody, toUserEmail, emailTopic);
    }


}
