package com.wayz.service.impl;

import com.wayz.model.User;
import com.wayz.repository.UserRepository;
import com.wayz.service.UserUpdateService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Сервис с логикой обновления пользователей
 */
@Service
@Data
public class UserUpdateServiceImpl implements UserUpdateService {

    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * Репозиторий пользователей
     */
    private final UserRepository userRepository;

    public UserUpdateServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Обновление данных о пользователе
     *
     * @param userToUpdateDetails новые данные пользователя для его обновления
     */
    @Override
    public User updateUser(User userToUpdateDetails) {
        Optional<User> optionalCurrentUser = userRepository.findByLogin(userToUpdateDetails.getLogin());
        User userToUpdate = optionalCurrentUser.orElseThrow(() -> {
            log.error("User update error. Can't find user by login: {}", userToUpdateDetails.getLogin());
            return new IllegalArgumentException("User update error. Can't find user by login: " + userToUpdateDetails.getLogin());
        });

        copyNonNullProperties(userToUpdateDetails, userToUpdate);

        userRepository.save(userToUpdate);
        log.info("User updated successfully: {}", userToUpdateDetails.getLogin());
        return userToUpdate;
    }

    /**
     * Копирование ненулевых свойств из источника в целевой объект.
     *
     * @param source источник данных
     * @param target целевой объект
     */
    private void copyNonNullProperties(User source, User target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    /**
     * Получение массива имен свойств, которые имеют значение null.
     *
     * @param source объект для проверки
     * @return массив имен свойств с null значениями
     */
    private String[] getNullPropertyNames(User source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

}
