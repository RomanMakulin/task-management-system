package com.wayz.service.actionsWithOrder;

import com.wayz.dto.CreateOrderDto;
import com.wayz.model.Order;
import org.springframework.http.ResponseEntity;

public interface CreateOrderService {
    ResponseEntity<Order> createOrder(CreateOrderDto createOrderDto, String token);
}
