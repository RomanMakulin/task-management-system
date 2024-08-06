package com.wayz.service.impl;

import com.wayz.dto.*;
import com.wayz.model.Order;
import com.wayz.model.submodels.OrderStatus;
import com.wayz.repository.OrderRepository;
import com.wayz.service.*;
import com.wayz.service.actionsWithOrder.CreateOrderService;
import com.wayz.service.actionsWithOrder.DeleteOrderService;
import com.wayz.service.actionsWithOrder.UpdateOrderService;
import com.wayz.service.clientUser.UserServiceClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Сервис с логикой управления заказами
 */
@Service
public class OrderServiceImpl implements OrderService {

    /**
     * Репозиторий заказов
     */
    private final OrderRepository orderRepository;

    /**
     * Сервис клиента работы с User-service (интеграция)
     */
    private final UserServiceClient userServiceClient;


    private final UpdateOrderService updateOrderService;

    private final DeleteOrderService deleteOrderService;

    /**
     * Сервис создания заказов
     */
    private final CreateOrderService createOrderService;

    public OrderServiceImpl(OrderRepository orderRepository,
                            UserServiceClient userServiceClient,
                            UpdateOrderService updateOrderService, DeleteOrderService deleteOrderService,
                            CreateOrderService createOrderService) {
        this.orderRepository = orderRepository;
        this.userServiceClient = userServiceClient;
        this.updateOrderService = updateOrderService;
        this.deleteOrderService = deleteOrderService;
        this.createOrderService = createOrderService;
    }


    /**
     * Создание заказа с проверкой существует ли такой пользователь в системе
     *
     * @param createOrderDto данные заказа
     * @param token          токен авторизации
     * @return созданный заказ
     */
    @Override
    public ResponseEntity<Order> createOrder(CreateOrderDto createOrderDto, String token) {
        return createOrderService.createOrder(createOrderDto, token);
    }

    /**
     * Установить заказу статус "Удален"
     * При этом нет удаления заказа из базы данных
     *
     * @param orderId идентификатор заказа
     * @return обновленный заказ
     */
    @Override
    public ResponseEntity<Order> deleteOrder(Long orderId) {
        return deleteOrderService.deleteOrder(orderId);
    }

    /**
     * Обновление заказа
     *
     * @param orderDetails данные для обновления заказа
     * @return обновленный заказ
     */
    @Override
    public ResponseEntity<Order> updateOrder(UpdateOrderDto orderDetails, String token) {
        return updateOrderService.updateOrder(orderDetails, token);
    }

    /**
     * Поиск заказа по id
     *
     * @param id order ID
     * @return объект заказа если найдет
     */
    @Override
    public Order findOrderByIdFromDto(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found for given ID"));
    }

    @Override
    public Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NullPointerException("Объект заказа не найден. ID: " + id));
    }

    /**
     * Изменить владельца заказа
     *
     * @param orderId  идентификатор заказа
     * @param username логин нового владельца заказа
     * @param token    токен авторизации
     * @return статус обновления заказа
     */
    @Override
    public ResponseEntity<Order> changeOwner(Long orderId, String username, String token) {
        Order order = findOrderByIdFromDto(orderId);
        User user = userServiceClient.getUserByLogin(username, token);
        order.setUserId(user.getID());
        orderRepository.save(order);
        return ResponseEntity.ok(order);
    }

    /**
     * Показать список всех заказов пользователя
     *
     * @param username логин пользователя
     * @param token    токен авторизации запроса
     * @return статус выполнения - лист заказов
     */
    @Override
    public ResponseEntity<List<Order>> getUserOrders(String username, String token) {
        User user = userServiceClient.getUserByLogin(username, token);
        return ResponseEntity.ok(orderRepository.findAll().stream()
                .filter(i -> i.getUserId().equals(user.getID())).toList());
    }

}
