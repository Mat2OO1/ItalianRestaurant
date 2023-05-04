package com.example.italianrestaurant.order;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Order status is mandatory")
    private OrderStatus orderStatus;
    private LocalDateTime deliveryDate;
    @NotBlank(message = "Order id is mandatory")
    private long orderId;
}
