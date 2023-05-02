package com.example.italianrestaurant.order.mealorder;

import com.example.italianrestaurant.exceptions.InvalidEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MealOrderService {

    private final MealOrderRepository mealOrderRepository;

    public boolean isMealOrderValid(MealOrder mealOrder) {
        return mealOrder.getMeal() != null &&
                mealOrder.getQuantity() > 0 &&
                mealOrder.getPrice() > 0;
    }


    public MealOrder addMealOrder(MealOrder mealOrder) throws InvalidEntityException {
        if (!isMealOrderValid(mealOrder)) throw new InvalidEntityException();
        return mealOrderRepository.save(mealOrder);
    }
}
