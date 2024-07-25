package com.wayz.controller;

import com.wayz.model.Order;
import com.wayz.service.SortingService;
import org.springframework.format.annotation.DateTimeFormat;
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
    public ResponseEntity<List<Order>> getOrdersByDateRange(@RequestParam("startDate") @DateTimeFormat(pattern = "dd.MM.yyyy, HH:mm") ZonedDateTime startDate,
                                                            @RequestParam("endDate") @DateTimeFormat(pattern = "dd.MM.yyyy, HH:mm") ZonedDateTime endDate) {
        return sortingService.searchByDateRange(startDate, endDate);
    }

    /**
     * Получить список заказов по указанной дате
     *
     * @param date нужная дата
     * @return список заказов если есть
     */
    @GetMapping("/get-orders-by-date")
    public ResponseEntity<List<Order>> getOrdersByDate(@RequestParam("date") @DateTimeFormat(pattern = "dd.MM.yyyy, HH:mm") ZonedDateTime date) {
        return sortingService.searchByDate(date);
    }

    /**
     * Запрос в БД на получение всех заказов в отсортированном виде по дате (по новизне)
     *
     * @return отсортированный список заказов
     */
    @GetMapping("/get-all-sorted-orders-by-date")
    public ResponseEntity<List<Order>> getAllSortedOrdersByDate() {
        return sortingService.getAllOrdersSortedByDate();
    }

    /**
     * Запрос в БД на получение заказов со статусом "UPDATED"
     *
     * @return отфильтрованный список заказов
     */
    @GetMapping("/get-updated-orders")
    public ResponseEntity<List<Order>> getUpdatedOrders() {
        return sortingService.getOrdersWithStatusUpdated();
    }

    /**
     * Запрос в БД на получение заказов со статусом "CREATED"
     *
     * @return отфильтрованный список заказов
     */
    @GetMapping("/get-created-orders")
    public ResponseEntity<List<Order>> getCreatedOrders() {
        return sortingService.getOrdersWithStatusCreated();
    }

}
