package com.wayz.service;

import com.wayz.model.User;

public interface UserInfoService {
    User getUserById(Long id);

    User getUserByLogin(String login);

    User getCurrentUser(String token);

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

    boolean existsById(Long id);
}
