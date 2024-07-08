package com.wayz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderAddress {
    private String country;
    private String city;
    private String street;
    private String index;
    private String description;
}
