package com.wayz.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wayz.model.submodels.OrderAddress;
import com.wayz.model.submodels.OrderItem;
import com.wayz.model.submodels.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.ArrayList;
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

    @JsonProperty
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderHistory> orderHistoryList = new ArrayList<>();

    /**
     * Добавление товара в список товаров заказа
     *
     * @param item новый товар
     */
    public void addItem(OrderItem item) {
        items.add(item);
    }

    public void addHistory(OrderHistory order) {
        this.orderHistoryList.add(order);
    }

    @Override
    public String toString() {
        return "Order{" +
                "ID=" + ID +
                ", userId=" + userId +
                ", orderDate=" + orderDate +
                ", status=" + status +
                ", orderAddress=" + orderAddress +
                ", items=" + items +
                '}';
    }
}
