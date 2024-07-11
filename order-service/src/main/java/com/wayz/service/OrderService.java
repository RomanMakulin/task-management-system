package com.wayz.service;

import com.wayz.dto.CreateOrderDto;
import com.wayz.dto.UpdateOrderDto;
import com.wayz.model.Order;

public interface OrderService {
    Order createOrder(CreateOrderDto createOrderDto);
    Order updateOrder(UpdateOrderDto orderDetails);
    Order getOrderById(Long id);
}
