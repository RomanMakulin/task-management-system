package com.wayz.model.orderSubmodels;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderStatus {
    @JsonProperty
    CREATED,

    @JsonProperty
    UPDATED,

    @JsonProperty
    DELETED
}
