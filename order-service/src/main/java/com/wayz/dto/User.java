package com.wayz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;

/**
 * DTO для определения пользователя
 */
@Data
public class User {

    @JsonProperty
    private Long ID;

    @JsonProperty
    private String login;

    @JsonProperty
    private String password;

    @JsonProperty
    private String firstName;

    @JsonProperty
    private String lastName;

    @JsonProperty
    private String email;

    @JsonProperty
    private String phone;

}
