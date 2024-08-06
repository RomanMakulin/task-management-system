package com.wayz.service.orderItems;

import com.wayz.dto.AddItemInOrderDto;
import com.wayz.model.Order;
import org.springframework.http.ResponseEntity;

public interface OrderItemService {
    ResponseEntity<Order> addItemInOrder(AddItemInOrderDto orderDetails);

    ResponseEntity<String> deleteItemFromOrder(Long orderId, String itemName);
}
