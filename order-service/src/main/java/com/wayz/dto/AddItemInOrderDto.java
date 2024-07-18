package com.wayz.dto;

import com.wayz.model.submodels.OrderItem;
import lombok.Data;

@Data
public class AddItemInOrderDto {
    private Long orderId;
    private OrderItem orderItem;
}
