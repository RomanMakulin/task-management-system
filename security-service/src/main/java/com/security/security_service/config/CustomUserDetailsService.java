package com.security.security_service.config;

import com.security.security_service.model.User;
import com.security.security_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Кастомный класс от UserDetailsService, необходимый для реализации security
 * Содержит репозиторий пользователей и находит юзера по логину для авторизации
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Репозиторий пользователей
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Нахождение пользователя по логину для авторизации
     *
     * @param username логин пользователя
     * @return информация о пользователе
     * @throws UsernameNotFoundException ошибка нахождения пользователя по логину
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(username);
        return user.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
