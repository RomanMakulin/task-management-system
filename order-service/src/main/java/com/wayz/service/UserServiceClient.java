package com.wayz.service;

import com.wayz.dto.User;

public interface UserServiceClient {
    User getUserById(Long userId);
    User getUserByLogin(String userLogin);
}
