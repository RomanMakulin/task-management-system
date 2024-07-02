package com.wayz.service;

import com.wayz.model.User;

public interface UserService {
    void registerUser(User user);
    User updateUser(User userDetails);
    User getUserById(Long id);
    User getUserByLogin(String login);
}
