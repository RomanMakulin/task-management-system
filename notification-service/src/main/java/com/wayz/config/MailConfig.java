package com.wayz.config;

import org.springframework.context.annotation.Configuration;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Configuration
public class MailConfig {

    private Session session;
    private final String host = "smtp.gmail.com";
    private final String port = "587";
    private final String username = "sup.makulin@gmail.com";
    private final String password = "wxamekdwpvpfpskp";

    /**
     * Конструктор сервиса
     */
    public MailConfig() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", this.host);
        props.put("mail.smtp.port", this.port);
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        this.session = Session.getInstance(props, new javax.mail.Authenticator() {
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
    public void messageManage(String emailTo, String textMessage, String titleMessage) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
            message.setSubject(titleMessage);
            message.setText(textMessage);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
