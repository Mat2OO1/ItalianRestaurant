package com.example.italianrestaurant.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeOrderStatusDto {
    private OrderStatus orderStatus;
    private LocalDateTime deliveryDate;
    private long orderId;
}
