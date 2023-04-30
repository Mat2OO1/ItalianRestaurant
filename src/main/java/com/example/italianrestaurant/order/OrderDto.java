package com.example.italianrestaurant.order;

import com.example.italianrestaurant.delivery.DeliveryDto;
import com.example.italianrestaurant.order.mealorder.MealOrder;
import com.example.italianrestaurant.order.mealorder.MealOrderDto;
import lombok.Data;

import java.util.List;

@Data
public class OrderDto {
    private DeliveryDto delivery;
    private List<MealOrderDto> mealOrders;
    private OrderStatus orderStatus;
}
