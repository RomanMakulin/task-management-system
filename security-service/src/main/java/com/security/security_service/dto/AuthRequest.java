package com.security.security_service.dto;

import lombok.Data;

/**
 * Класс DTO для авторизации пользователя
 */
@Data
public class AuthRequest {
    private String login;
    private String password;
}
