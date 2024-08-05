package com.wayz.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wayz.model.submodels.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "order_history")
@Data
/**
 * Таблица истории заказов
 */
public class OrderHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID ID;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "changed_data")
    private String changedData;

    @Column(name = "change_date")
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    @JsonProperty
    @Column(name = "old_status")
    private OrderStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @JsonProperty
    @Column(name = "new_status")
    private OrderStatus newStatus;

    /**
     * Кастомный конструктор создания истории заказа
     *
     * @param order     заказ
     * @param newStatus новый статус заказа
     */
    public OrderHistory(Order order, OrderStatus newStatus) {
        this.order = order;
        this.changedData = order.toString().substring(0, 250);
        this.dateTime = LocalDateTime.now();
        this.oldStatus = order.getStatus();

        if (newStatus == null) {
            this.newStatus = order.getStatus();
        } else this.newStatus = newStatus;
    }

    public OrderHistory() {

    }

    @Override
    public String toString() {
        return "OrderHistory{" +
                "ID=" + ID +
                ", orderId=" + order.getID() +
                ", changedData='" + changedData + '\'' +
                ", dateTime=" + dateTime +
                ", oldStatus=" + oldStatus +
                ", newStatus=" + newStatus +
                '}';
    }
}
