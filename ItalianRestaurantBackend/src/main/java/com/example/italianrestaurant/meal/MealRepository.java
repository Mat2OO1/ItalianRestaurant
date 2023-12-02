package com.example.italianrestaurant.meal;

import com.example.italianrestaurant.meal.mealcategory.MealCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MealRepository extends JpaRepository<Meal, Long> {
    Page<Meal> findAll(Pageable pageable);
    List<Meal> getAllByDeletedFalse();
    boolean existsByName(String name);

    Page<Meal> getMealsByMealCategoryName(Pageable pageable, String name);

    Optional<Meal> findByNameAndDeletedIsFalse(String name);
}
