package com.example.italianrestaurant.meal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MealRepository extends JpaRepository<Meal, Long> {
    Page<Meal> findAll(Pageable pageable);
    boolean existsByName(String name);

    Page<Meal> getMealsByMealCategoryName(Pageable pageable, String name);
}
