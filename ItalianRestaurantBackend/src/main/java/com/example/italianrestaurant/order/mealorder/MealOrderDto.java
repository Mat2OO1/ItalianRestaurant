package com.example.italianrestaurant.order.mealorder;

import com.example.italianrestaurant.meal.Meal;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealOrderDto {
    @NotBlank(message = "Meal is mandatory")
    private Meal meal;
    @Min(1)
    private int quantity;
    @Min(0)
    private double price;
}
