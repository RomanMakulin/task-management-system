package com.wayz.model.submodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
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
