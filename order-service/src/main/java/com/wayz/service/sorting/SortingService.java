package com.wayz.service.sorting;

import com.wayz.model.Order;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.List;

public interface SortingService {

    ResponseEntity<List<Order>> searchByDateRange(ZonedDateTime startDate, ZonedDateTime endDate);

    ResponseEntity<List<Order>> searchByDate(ZonedDateTime needDate);

    ResponseEntity<List<Order>> getAllOrdersSortedByDate();

    ResponseEntity<List<Order>> getOrdersWithStatusUpdated();

    ResponseEntity<List<Order>> getOrdersWithStatusCreated();

}
