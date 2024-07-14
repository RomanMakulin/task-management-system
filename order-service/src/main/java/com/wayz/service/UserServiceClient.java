package com.wayz.service;

import com.wayz.dto.User;

public interface UserServiceClient {
    User getUserById(Long userId, String token);
    User getUserByLogin(String userLogin, String token);
}
