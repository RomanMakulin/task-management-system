package com.wayz.service.actionsWithOrder;

import com.wayz.model.Order;
import org.springframework.http.ResponseEntity;

public interface DeleteOrderService {
    ResponseEntity<Order> deleteOrder(Long orderId);
}
