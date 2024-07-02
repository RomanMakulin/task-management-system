package com.wayz.model.submodels;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Embeddable
public class OrderItem {
    private String name;
    private int quantity;
    private BigDecimal price;
}
