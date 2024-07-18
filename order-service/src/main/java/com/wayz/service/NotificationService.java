package com.wayz.service;

import com.wayz.dto.User;
import com.wayz.model.Order;

public interface NotificationService {
    void orderNotifyKafka(Order order, User user);
}
