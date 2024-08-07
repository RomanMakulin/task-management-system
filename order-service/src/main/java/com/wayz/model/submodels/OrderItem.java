package com.wayz.model.submodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Embeddable
public class OrderItem {

    @JsonProperty
    private String name;

    @JsonProperty
    private int quantity;

    @JsonProperty
    private BigDecimal price;
}
