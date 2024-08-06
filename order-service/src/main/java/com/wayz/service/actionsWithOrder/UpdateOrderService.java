package com.wayz.service.actionsWithOrder;

import com.wayz.dto.UpdateOrderDto;
import com.wayz.model.Order;
import org.springframework.http.ResponseEntity;

public interface UpdateOrderService {
    ResponseEntity<Order> updateOrder(UpdateOrderDto orderDetails, String token);
}
