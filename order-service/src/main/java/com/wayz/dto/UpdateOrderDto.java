package com.wayz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wayz.model.submodels.OrderAddress;
import com.wayz.model.submodels.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class UpdateOrderDto {

    @JsonProperty
    private Long id;

    @JsonProperty
    private String login;

    @JsonProperty
    private OrderAddress orderAddress;

    @JsonProperty
    private List<OrderItem> items;
}
