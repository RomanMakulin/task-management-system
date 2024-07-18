package com.wayz.config.impl;

import com.wayz.config.MailConfig;
import jakarta.annotation.PostConstruct;
import jakarta.mail.internet.AddressException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

@Configuration
public class MailConfigImpl implements MailConfig {

    @Value("${mail.config.host}")
    private String host;

    @Value("${mail.config.port}")
    private String port;

    @Value("${mail.config.username}")
    private String username;

    @Value("${mail.config.password}")
    private String password;

    private Session session;

    /**
     * Конструктор сервиса
     */
    @PostConstruct
    public void init() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", this.host);
        props.put("mail.smtp.port", this.port);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        this.session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    /**
     * Общая настройка отправки почты (сообщение)
     *
     * @param emailTo      почта получателя
     * @param textMessage  текст сообщения
     * @param titleMessage заголовок сообщения
     */
    @Override
    public void messageManage(String emailTo, String textMessage, String titleMessage) {
        try {
            // Проверка формата email
            InternetAddress emailAddr = new InternetAddress(emailTo);
            emailAddr.validate();

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
            message.setSubject(titleMessage);
            message.setText(textMessage);
            Transport.send(message);
        } catch (AddressException ex) {
            throw new RuntimeException("Неправильный формат email адреса: " + emailTo, ex);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
