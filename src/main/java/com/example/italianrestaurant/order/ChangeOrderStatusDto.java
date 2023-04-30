package com.example.italianrestaurant.order;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChangeOrderStatusDto {
    private OrderStatus orderStatus;
    private LocalDateTime deliveryDate;
    private long orderId;
}
