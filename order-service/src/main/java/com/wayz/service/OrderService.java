package com.wayz.service;

import com.wayz.dto.AddItemInOrderDto;
import com.wayz.dto.CreateOrderDto;
import com.wayz.dto.UpdateOrderDto;
import com.wayz.model.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface OrderService {
    ResponseEntity<Order> createOrder(CreateOrderDto createOrderDto, String token);

    ResponseEntity<Order> updateOrder(UpdateOrderDto orderDetails, String token);

    Order findOrderByIdFromDto(Long id);

    ResponseEntity<Order> changeOwner(Long orderId, String username, String token);

    ResponseEntity<List<Order>> getUserOrders(String username, String token);

}
