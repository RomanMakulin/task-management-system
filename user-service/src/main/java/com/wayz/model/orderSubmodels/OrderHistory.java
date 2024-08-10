package com.wayz.model.orderSubmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wayz.model.Order;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OrderHistory {

    @JsonProperty
    private UUID ID;

    @JsonProperty
    private Order order;

    @JsonProperty
    private String changedData;

    @JsonProperty
    private LocalDateTime dateTime;

    @JsonProperty
    private OrderStatus oldStatus;

    @JsonProperty
    private OrderStatus newStatus;
}
