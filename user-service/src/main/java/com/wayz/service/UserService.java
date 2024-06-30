package com.wayz.service;

import com.wayz.model.User;

public interface UserService {
    void registerUser(User user);
    void updateUser(User userDetails);
    User getUserById(Long id);
}
