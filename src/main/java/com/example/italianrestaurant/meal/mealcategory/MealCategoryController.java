package com.example.italianrestaurant.meal.mealcategory;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/meal-categories")
@RequiredArgsConstructor
public class MealCategoryController {

    private final MealCategoryService mealCategoryService;

    @GetMapping
    public ResponseEntity<List<MealCategory>> getAllMealCategories() {
        return ResponseEntity.ok(mealCategoryService.getAllMealCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealCategory> getMealById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(mealCategoryService.getMealById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
