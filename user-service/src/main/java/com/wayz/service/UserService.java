package com.wayz.service;

import com.wayz.model.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    User updateUser(User userDetails);
    User getUserById(Long id);
    User getUserByLogin(String login);
}
