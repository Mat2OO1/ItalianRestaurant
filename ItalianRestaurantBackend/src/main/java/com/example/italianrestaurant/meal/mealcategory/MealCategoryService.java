package com.example.italianrestaurant.meal.mealcategory;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MealCategoryService {

    private final MealCategoryRepository mealCategoryRepository;

    public List<MealCategory> getAllMealCategories() {
        return mealCategoryRepository.findAll();
    }

    public MealCategory getMealById(Long id) {
        Optional<MealCategory> meal = mealCategoryRepository.findById(id);
        return meal.orElseThrow(EntityNotFoundException::new);
    }

    public MealCategory getMealCategoryByName(String name) throws EntityNotFoundException{
        Optional<MealCategory> mealCategory = mealCategoryRepository.findByName(name);
        return mealCategory.orElseThrow(EntityNotFoundException::new);
    }
}
