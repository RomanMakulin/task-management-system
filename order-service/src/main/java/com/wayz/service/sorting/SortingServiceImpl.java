package com.wayz.service.sorting;

import com.wayz.model.Order;
import com.wayz.repository.OrderRepository;
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

    /**
     * Получить список заказов по указанной дате
     *
     * @param needDate нужная дата
     * @return список заказов если есть
     */
    @Override
    public ResponseEntity<List<Order>> searchByDate(ZonedDateTime needDate) {
        List<Order> orderList = orderRepository.getOrderByDate(needDate)
                .orElseThrow(() -> new NullPointerException("Нет заказов по выбранной дате."));
        return ResponseEntity.ok(orderList);
    }

    /**
     * Получить все заказы в отсортированном виде по дате (по новизне)
     *
     * @return отсортированный список заказов
     */
    @Override
    public ResponseEntity<List<Order>> getAllOrdersSortedByDate() {
        return ResponseEntity.ok(orderRepository.getAllSortedByDate());
    }

    /**
     * Запрос в БД на получение заказов со статусом "UPDATED"
     *
     * @return отфильтрованный список заказов
     */
    @Override
    public ResponseEntity<List<Order>> getOrdersWithStatusUpdated() {
        return ResponseEntity.ok(orderRepository.getUpdatedOrders());
    }

    /**
     * Запрос в БД на получение заказов со статусом "CREATED"
     *
     * @return отфильтрованный список заказов
     */
    @Override
    public ResponseEntity<List<Order>> getOrdersWithStatusCreated() {
        return ResponseEntity.ok(orderRepository.getCreatedOrders());
    }


}
