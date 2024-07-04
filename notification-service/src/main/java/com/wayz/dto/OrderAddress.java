package com.wayz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderAddress {

    @JsonProperty
    private String country;

    @JsonProperty
    private String city;

    @JsonProperty
    private String street;

    @JsonProperty
    private String index;

    @JsonProperty
    private String description;

}
