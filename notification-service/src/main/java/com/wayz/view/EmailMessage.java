package com.wayz.view;

import com.wayz.dto.Notification;
import com.wayz.dto.OrderItem;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Data
@Component
public class EmailMessage {

    public String create(Notification notification) {
        StringBuilder sb = new StringBuilder();

        sb.append("Уведомление о заказе\n");
        sb.append("=================================\n");
        sb.append("Логин: ").append(notification.getLogin()).append("\n");
        sb.append("Имя: ").append(notification.getFirstName()).append("\n");
        sb.append("Фамилия: ").append(notification.getLastName()).append("\n");
        sb.append("Email: ").append(notification.getEmail()).append("\n");
        sb.append("Телефон: ").append(notification.getPhone()).append("\n");
        sb.append("=================================\n");
        sb.append("Информация о заказе\n");
        sb.append("ID заказа: ").append(notification.getOrder().getID()).append("\n");
        sb.append("ID пользователя: ").append(notification.getOrder().getUserId()).append("\n");
        sb.append("Дата заказа: ").append(notification.getOrder().getOrderDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))).append("\n");
        sb.append("Статус: ").append(notification.getOrder().getStatus()).append("\n");
        sb.append("Адрес:\n");
        sb.append("  Страна: ").append(notification.getOrder().getOrderAddress().getCountry()).append("\n");
        sb.append("  Город: ").append(notification.getOrder().getOrderAddress().getCity()).append("\n");
        sb.append("  Улица: ").append(notification.getOrder().getOrderAddress().getStreet()).append("\n");
        sb.append("  Индекс: ").append(notification.getOrder().getOrderAddress().getIndex()).append("\n");
        sb.append("  Описание: ").append(notification.getOrder().getOrderAddress().getDescription()).append("\n");
        sb.append("=================================\n");
        sb.append("Товары:\n");

        for (OrderItem item : notification.getOrder().getItems()) {
            sb.append("  Название: ").append(item.getName()).append("\n");
            sb.append("  Количество: ").append(item.getQuantity()).append("\n");
            sb.append("  Цена: ").append(item.getPrice()).append(" руб.\n");
            sb.append("---------------------------------\n");
        }

        return sb.toString();
    }

}
