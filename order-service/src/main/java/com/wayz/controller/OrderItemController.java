package com.wayz.controller;

import com.wayz.dto.AddItemInOrderDto;
import com.wayz.model.Order;
import com.wayz.service.orderItems.OrderItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderItemController {

    /**
     * Сервис управления товарами в заказе
     */
    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    /**
     * Добавление нового товара в заказ
     *
     * @param addItemInOrderDto тело запроса
     * @return статус ответа (новый товар)
     */
    @PostMapping("/add-item")
    public ResponseEntity<Order> addItemInOrder(@RequestBody AddItemInOrderDto addItemInOrderDto) {
        return orderItemService.addItemInOrder(addItemInOrderDto);
    }

    /**
     * Удаление товара из заказа
     *
     * @param orderId  идентификатор заказа
     * @param itemName название товара
     * @return статус удаления
     */
    @PostMapping("/delete-item")
    public ResponseEntity<String> deleteItemInOrder(@RequestParam Long orderId,
                                                    @RequestParam String itemName) {
        return orderItemService.deleteItemFromOrder(orderId, itemName);
    }

}
