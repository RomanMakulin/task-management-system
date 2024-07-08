package com.wayz.service;

import com.wayz.dto.AuthDdo;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    boolean checkPassword(AuthDdo currentUserDetails);
    void setCookie(AuthDdo authDdo, HttpServletResponse response);
    String getSecretKey();
}
