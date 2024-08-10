package com.wayz.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wayz.model.orderSubmodels.OrderAddress;
import com.wayz.model.orderSubmodels.OrderHistory;
import com.wayz.model.orderSubmodels.OrderItem;
import com.wayz.model.orderSubmodels.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.ArrayList;
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
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @JsonProperty
    private OrderAddress orderAddress;

    @JsonProperty
    private List<OrderItem> items;

    @JsonProperty
    private List<OrderHistory> orderHistoryList = new ArrayList<>();
}
