package com.wayz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItem {
    private String name;
    private int quantity;
    private BigDecimal price;
}
