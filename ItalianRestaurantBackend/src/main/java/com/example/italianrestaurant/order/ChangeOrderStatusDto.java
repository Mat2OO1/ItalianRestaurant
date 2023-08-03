package com.example.italianrestaurant.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Order status is mandatory")
    private OrderStatus orderStatus;
    private LocalDateTime deliveryDate;
    @Min(value = 1, message = "Order id must be greater than 0")
    private long orderId;
}
