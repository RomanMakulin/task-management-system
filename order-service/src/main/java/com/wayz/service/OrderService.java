package com.wayz.service;

import com.wayz.dto.AddItemInOrderDto;
import com.wayz.dto.CreateOrderDto;
import com.wayz.dto.UpdateOrderDto;
import com.wayz.model.Order;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    Order createOrder(CreateOrderDto createOrderDto, String token);
    Order updateOrder(UpdateOrderDto orderDetails, String token);
    Order getOrderById(Long id, String token);
    ResponseEntity<String> addItemInOrder(AddItemInOrderDto orderDetails);
    ResponseEntity<String> deleteItemFromOrder(Long orderId, String itemName);
}
