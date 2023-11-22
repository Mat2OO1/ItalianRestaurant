package com.example.italianrestaurant.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MealOrderEmail {
    private String mealName;
    private long quantity;
    private String price;
}
