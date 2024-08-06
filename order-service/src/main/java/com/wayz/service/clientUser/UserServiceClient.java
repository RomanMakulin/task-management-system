package com.wayz.service.clientUser;

import com.wayz.dto.User;

public interface UserServiceClient {
    User getUserById(Long userId, String token);

    User getUserByLogin(String userLogin, String token);
}
