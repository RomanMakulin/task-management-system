package com.wayz.service;

import com.wayz.model.Order;

import java.util.List;

public interface UserOrderService {
    List<Order> getCurrentUserOrderList(String token);
}
