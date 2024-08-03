package com.wayz.repository;

import com.wayz.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Запрос в БД на получение списка заказов в выбранном диапазоне дат
     *
     * @param startDate начальная дата
     * @param endDate   конечная дата
     * @return список заказов
     */
    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    Optional<List<Order>> getOrderByDateRange(@Param("startDate") ZonedDateTime startDate, @Param("endDate") ZonedDateTime endDate);

    /**
     * Запрос в БД на получение списка заков по конкретной дате
     *
     * @param needDate дата заказов
     * @return список заказа
     */
    @Query("SELECT o FROM Order o WHERE o.orderDate = :needDate")
    Optional<List<Order>> getOrderByDate(@Param("needDate") ZonedDateTime needDate);

    /**
     * Запрос в БД на получение всех заказов в отсортированном виде по дате (по новизне)
     *
     * @return отсортированный список заказов
     */
    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC ")
    List<Order> getAllSortedByDate();

    /**
     * Запрос в БД на получение заказов со статусом "UPDATED"
     *
     * @return отфильтрованный список заказов
     */
    @Query("SELECT o from Order o WHERE o.status = com.wayz.model.submodels.OrderStatus.UPDATED")
    List<Order> getUpdatedOrders();

    /**
     * Запрос в БД на получение заказов со статусом "CREATED"
     *
     * @return отфильтрованный список заказов
     */
    @Query("SELECT o from Order o WHERE o.status = com.wayz.model.submodels.OrderStatus.CREATED")
    List<Order> getCreatedOrders();


}
