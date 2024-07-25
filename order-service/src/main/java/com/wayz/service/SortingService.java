package com.wayz.service;

import com.wayz.model.Order;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.util.List;

public interface SortingService {

    ResponseEntity<List<Order>> searchByDateRange(ZonedDateTime startDate, ZonedDateTime endDate);

}
