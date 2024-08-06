package com.wayz.service.orderItems;

import com.wayz.dto.AddItemInOrderDto;
import com.wayz.model.Order;
import com.wayz.model.submodels.OrderItem;
import com.wayz.repository.OrderRepository;
import com.wayz.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    /**
     * Logger
     */
    private static final Logger log = LoggerFactory.getLogger(OrderItemServiceImpl.class);

    /**
     * Сервис управления заказами
     */
    private final OrderService orderService;

    /**
     * Репозиторий заказов
     */
    private final OrderRepository orderRepository;

    public OrderItemServiceImpl(OrderService orderService,
                                OrderRepository orderRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    /**
     * Добавить новый товар в существующий заказ
     *
     * @param orderDetails данные из dto для добавления нового товара в заказ
     * @return статус выполнения запроса
     */
    @Override
    public ResponseEntity<Order> addItemInOrder(AddItemInOrderDto orderDetails) {
        try {
            Order order = orderService.findOrderByIdFromDto(orderDetails.getOrderId());
            order.addItem(orderDetails.getOrderItem());
            orderRepository.save(order);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            log.info("Error while adding order into order service: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Удаление конкретного товара по его имени в заказе
     *
     * @param orderId  идентификатор заказа
     * @param itemName имя товара
     * @return статус выполнения запроса
     */
    @Override
    public ResponseEntity<String> deleteItemFromOrder(Long orderId, String itemName) {
        try {
            Order order = orderService.findOrderByIdFromDto(orderId);
            Optional<OrderItem> orderItem = findItemByName(order, itemName);

            orderItem.ifPresent(item -> checkItemListAndDelete(order, item));
            return ResponseEntity.badRequest().body("Item not found. Name: " + itemName);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Удаление товара из заказа
     * Если список товаров после удаления последнего пустой - удаляется и сам заказ
     *
     * @param order     текущий заказ
     * @param orderItem товар в заказе для удаления
     */
    public void checkItemListAndDelete(Order order, OrderItem orderItem) {
        order.getItems().remove(orderItem);
        orderRepository.save(order);

        if (order.getItems().isEmpty()) {
            orderRepository.delete(order); // удаляем заказ если список товаров в нем пустой
            ResponseEntity.ok("Товар удален: " + orderItem + "\n" + "Заказ удален так как в нем закончились товары.");
            return;
        }
        ResponseEntity.ok("Товар удален: " + orderItem);
    }

    /**
     * Поиск товара по названию в заказе
     *
     * @param order    нужный заказ
     * @param itemName название товара
     * @return необходимый товар если есть
     */
    public Optional<OrderItem> findItemByName(Order order, String itemName) {
        return order.getItems().stream()
                .filter(item -> item.getName().equals(itemName)).findFirst();
    }

}
