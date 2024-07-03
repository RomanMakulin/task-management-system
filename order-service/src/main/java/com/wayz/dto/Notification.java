package com.wayz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wayz.model.Order;
import lombok.Data;

@Data
public class Notification {
    @JsonProperty
    private String login;

    @JsonProperty
    private String firstName;

    @JsonProperty
    private String lastName;

    @JsonProperty
    private String email;

    @JsonProperty
    private String phone;

    @JsonProperty
    private Order order;
}
