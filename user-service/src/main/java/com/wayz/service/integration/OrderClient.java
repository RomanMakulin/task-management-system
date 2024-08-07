package com.wayz.service.integration;

import com.wayz.model.Order;

import java.util.List;

public interface OrderClient {
    List<Order> getOrderListByCurrentUser(String token);
}
