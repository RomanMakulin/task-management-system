package com.wayz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Notification {
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Order order;
}
