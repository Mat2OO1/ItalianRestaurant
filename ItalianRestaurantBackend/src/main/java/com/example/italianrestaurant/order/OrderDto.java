package com.example.italianrestaurant.order;

import com.example.italianrestaurant.delivery.DeliveryDto;
import com.example.italianrestaurant.order.mealorder.MealOrderDto;
import com.example.italianrestaurant.payments.Payment;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private DeliveryDto delivery;
    private long tableNr;

    @NotEmpty(message = "Meal orders are mandatory")
    private List<MealOrderDto> mealOrders;
    @NotNull(message = "Order status is mandatory")
    private OrderStatus orderStatus;
}
