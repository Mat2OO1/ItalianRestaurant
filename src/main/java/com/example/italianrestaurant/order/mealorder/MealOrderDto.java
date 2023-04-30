package com.example.italianrestaurant.order.mealorder;

import com.example.italianrestaurant.meal.Meal;
import lombok.Data;

@Data
public class MealOrderDto {
    private Meal meal;
    private int quantity;
    private double price;
}
