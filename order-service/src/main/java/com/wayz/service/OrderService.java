package com.wayz.service;

import com.wayz.model.Order;

public interface OrderService {
    Order createOrder(Order orderDetails);
    Order updateOrder(Order orderDetails);
    Order getOrderById(Long id);
}
