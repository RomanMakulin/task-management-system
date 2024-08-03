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
    @Column(name = "order_status")
    private OrderStatus orderStatus;

}
