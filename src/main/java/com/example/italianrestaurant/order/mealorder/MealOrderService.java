package com.example.italianrestaurant.order.mealorder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MealOrderService {

    public boolean isMealOrderValid(MealOrder mealOrder) {
        return mealOrder.getMeal() != null &&
                mealOrder.getQuantity() > 0 &&
                mealOrder.getPrice() > 0;
    }
}
