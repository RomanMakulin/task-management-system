package com.wayz.model.submodels;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class OrderAddress {
    private String country;
    private String city;
    private String street;
    private String index;
    private String description;
}
