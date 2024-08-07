package com.wayz.service.impl;

import com.wayz.model.User;
import com.wayz.repository.UserRepository;
import com.wayz.service.UserInfoService;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    /**
     * Репозиторий пользователей
     */
    private final UserRepository userRepository;

    public UserInfoServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Получение пользователя по ID
     *
     * @param id уникальный идентификатор
     * @return пользователь
     */
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Получение пользователя по логину из БД
     *
     * @param login логин для поиска
     * @return объект пользователя найденный из БД
     */
    @Override
    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с таким логином не найден"));
    }

    @Override
    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

}
