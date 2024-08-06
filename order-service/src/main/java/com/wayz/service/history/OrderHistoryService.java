package com.wayz.service.history;

import com.wayz.model.Order;
import com.wayz.model.OrderHistory;
import com.wayz.model.submodels.OrderStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderHistoryService {
    void updateOrderHistory(Order order, OrderStatus newStatus);
    ResponseEntity<List<OrderHistory>> getOrderHistoryByOrderId(Long orderId);
}
