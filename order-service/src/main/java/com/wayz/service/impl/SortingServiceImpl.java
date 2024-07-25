package com.wayz.service.impl;

import com.wayz.model.Order;
import com.wayz.repository.OrderRepository;
import com.wayz.service.SortingService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Сервис для фильтрации и сортировки заказов
 */
@Service
public class SortingServiceImpl implements SortingService {

    /**
     * Репозиторий заказов
     */
    private final OrderRepository orderRepository;

    public SortingServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Получить список заказов по диапазону дат
     *
     * @param startDate начальная дата для поиска
     * @param endDate   конечная дата для поиска
     * @return список заказов если такие есть
     */
    @Override
    public ResponseEntity<List<Order>> searchByDateRange(ZonedDateTime startDate, ZonedDateTime endDate) {
        List<Order> orderList = orderRepository.getOrderByDateRange(startDate, endDate)
                .orElseThrow(() -> new NullPointerException("Нет заказов в выбранном диапазоне дат."));
        return ResponseEntity.ok(orderList);
    }

}
