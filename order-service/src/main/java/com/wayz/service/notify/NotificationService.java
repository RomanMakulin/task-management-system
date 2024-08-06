package com.wayz.service.notify;

import com.wayz.dto.User;
import com.wayz.model.Order;

public interface NotificationService {
    void orderNotifyKafka(Order order, User user);
}
