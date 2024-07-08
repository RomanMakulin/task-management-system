package com.wayz.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class Order {
    @JsonAlias({"ID", "id"})
    private Long id; // Объединяем значения полей ID и id в одно поле
    private Long userId;
    private ZonedDateTime orderDate;
    private OrderStatus status;
    private OrderAddress orderAddress;
    private List<OrderItem> items;
}
