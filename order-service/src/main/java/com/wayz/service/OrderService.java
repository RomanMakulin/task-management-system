package com.wayz.service;

import com.wayz.dto.CreateOrderDto;
import com.wayz.dto.UpdateOrderDto;
import com.wayz.model.Order;

public interface OrderService {
    Order createOrder(CreateOrderDto createOrderDto, String token);
    Order updateOrder(UpdateOrderDto orderDetails, String token);
    Order getOrderById(Long id, String token);
}
