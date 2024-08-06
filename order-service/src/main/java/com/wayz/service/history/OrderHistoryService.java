package com.wayz.service.history;

import com.wayz.model.Order;
import com.wayz.model.submodels.OrderStatus;

public interface OrderHistoryService {
    void updateOrderHistory(Order order, OrderStatus newStatus);
}
