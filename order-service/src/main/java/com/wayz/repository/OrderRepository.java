package com.wayz.repository;

import com.wayz.model.Order;
import org.aspectj.weaver.ast.Or;
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

}
