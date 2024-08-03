package com.wayz.repository;

import com.wayz.model.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, UUID> {
}
