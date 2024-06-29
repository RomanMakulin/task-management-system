package com.wayz.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;

    private String login;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

}
