package com.example.italianrestaurant.meal.mealcategory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MealCategoryRepository extends JpaRepository<MealCategory, Long> {
    Optional<MealCategory> findByName(String name);

}
