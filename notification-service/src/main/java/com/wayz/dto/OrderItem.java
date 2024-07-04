package com.wayz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItem {

    @JsonProperty
    private String name;

    @JsonProperty
    private int quantity;

    @JsonProperty
    private BigDecimal price;

}
