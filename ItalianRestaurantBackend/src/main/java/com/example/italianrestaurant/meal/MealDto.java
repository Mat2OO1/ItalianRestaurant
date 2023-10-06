package com.example.italianrestaurant.meal;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MealDto {
    @NotNull(message = "Name is mandatory")
    private String name;
    @NotEmpty(message = "Image Path is mandatory")
    private String imgPath;
    @NotNull(message = "Category is mandatory")
    private String category;

    private String description;
    @NotNull(message = "Price is mandatory")
    private double price;
}
