package com.wayz.service;

import com.wayz.model.User;
import com.wayz.repository.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(User user) {
        userRepository.save(user); // TODO Доделать
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
