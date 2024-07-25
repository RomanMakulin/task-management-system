package com.wayz.controller;

import com.wayz.model.Order;
import com.wayz.service.OrderService;
import com.wayz.service.SortingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Контроллер для сортировки и фильтрации заказов
 */
@RestController
@RequestMapping("/orders")
public class SortingOrdersController {

    /**
     * Сервис для фильтрации и сортировки заказов
     */
    private final SortingService sortingService;

    public SortingOrdersController(SortingService sortingService) {
        this.sortingService = sortingService;
    }

    /**
     * Получить список заказов по диапазону дат
     *
     * @param startDate начальная дата для поиска
     * @param endDate   конечная дата для поиска
     * @return список заказов если такие есть
     */
    @GetMapping("/get-orders-by-date-range")
    public ResponseEntity<List<Order>> getOrdersByDate(@RequestParam ZonedDateTime startDate, @RequestParam ZonedDateTime endDate) {
        return sortingService.searchByDateRange(startDate, endDate);
    }

}
