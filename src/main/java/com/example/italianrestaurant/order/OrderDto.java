package com.example.italianrestaurant.order;

import com.example.italianrestaurant.delivery.DeliveryDto;
import com.example.italianrestaurant.order.mealorder.MealOrder;
import com.example.italianrestaurant.order.mealorder.MealOrderDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    @NotBlank(message = "Delivery is mandatory")
    private DeliveryDto delivery;
    @NotEmpty(message = "Meal orders are mandatory")
    private List<MealOrderDto> mealOrders;
    @NotBlank(message = "Order status is mandatory")
    private OrderStatus orderStatus;
}
