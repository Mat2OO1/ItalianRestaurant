package com.example.italianrestaurant.order.mealorder;

import com.example.italianrestaurant.meal.Meal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealOrderDto {
    private Meal meal;
    private int quantity;
    private double price;
}
