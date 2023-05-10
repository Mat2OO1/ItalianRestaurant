package com.example.italianrestaurant.order.mealorder;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MealOrderService {

    private final MealOrderRepository mealOrderRepository;


    public MealOrder addMealOrder(MealOrder mealOrder) {
        return mealOrderRepository.save(mealOrder);
    }
}
