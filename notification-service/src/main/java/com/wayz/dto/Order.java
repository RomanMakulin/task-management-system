package com.wayz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class Order {

    @JsonProperty
    private Long ID;

    @JsonProperty
    private Long userId;

    @JsonProperty
    private ZonedDateTime orderDate;

    @JsonProperty
    private OrderStatus status;

    @JsonProperty
    private OrderAddress orderAddress;

    @JsonProperty
    private List<OrderItem> items;
}
