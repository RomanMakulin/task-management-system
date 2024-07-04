package com.wayz.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty
    private Long ID;

    @Column(name = "user_id")
    @JsonProperty
    private Long userId;

    @Column(name = "order_date")
    @JsonProperty
    private ZonedDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @JsonProperty
    private OrderStatus status;

    @Embedded
    @JsonProperty
    private OrderAddress orderAddress;

    @ElementCollection
    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
    @OrderColumn(name = "order_item_order")
    @JsonProperty
    private List<OrderItem> items;

}
