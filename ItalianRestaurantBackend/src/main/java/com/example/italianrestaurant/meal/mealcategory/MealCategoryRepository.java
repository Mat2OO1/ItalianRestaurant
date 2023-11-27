package com.example.italianrestaurant.meal.mealcategory;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MealCategoryRepository extends JpaRepository<MealCategory, Long> {
    Optional<MealCategory> findByNameAndDeletedIsFalse(String name);
    Optional<MealCategory> findByIdAndDeletedIsFalse(Long id);
    List<MealCategory> getMealCategoriesByDeletedIsFalse();

}
