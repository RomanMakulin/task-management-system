package com.wayz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderStatus {

    @JsonProperty
    CREATED,

    @JsonProperty
    UPDATED,

    @JsonProperty
    DELETED

}
