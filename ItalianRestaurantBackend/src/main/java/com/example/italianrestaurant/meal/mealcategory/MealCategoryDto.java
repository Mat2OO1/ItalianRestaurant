package com.example.italianrestaurant.meal.mealcategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MealCategoryDto {
    private String name;
    private String imgPath;
}
