package com.wayz.service;

import com.wayz.model.User;
import org.springframework.http.ResponseEntity;

public interface UserService {
    User updateUser(User userDetails);

    ResponseEntity<String> deleteUser(Long id);
}
