package com.wayz.model;

import com.wayz.model.submodels.OrderAddress;
import com.wayz.model.submodels.OrderItem;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "order_date")
    private ZonedDateTime orderDate;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private OrderAddress orderAddress;

    @ElementCollection
    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
    @OrderColumn(name = "order_item_order")
    private List<OrderItem> items;

}
