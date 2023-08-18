package com.example.italianrestaurant.meal;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;

    public Page<Meal> getAllMeals(Pageable pageable) {
        return mealRepository.findAll(pageable);
    }

    public Meal getMealById(Long id) {
        Optional<Meal> meal = mealRepository.findById(id);

        return meal.orElseThrow(EntityNotFoundException::new);
    }
}
