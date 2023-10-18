package com.example.italianrestaurant.meal.mealcategory;

import com.example.italianrestaurant.exceptions.InvalidEntityException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/meal-categories")
@RequiredArgsConstructor
@Log4j2
public class MealCategoryController {

    private final MealCategoryService mealCategoryService;

    @GetMapping
    public ResponseEntity<List<MealCategory>> getAllMealCategories() {
        return ResponseEntity.ok(mealCategoryService.getAllMealCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealCategory> getMealCategoryById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(mealCategoryService.getMealCategoryById(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "There is no meal category with id: " + id, e);
        }
    }

    @PostMapping(path = "/add")
    public ResponseEntity<MealCategory> addCategory(@RequestPart("image") MultipartFile image,
                                                    @RequestPart("meal_category") MealCategoryDto mealCategoryDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(mealCategoryService.addCategory(mealCategoryDto, image));
        } catch (EntityExistsException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "There is already a category with name " + mealCategoryDto.getName(), e);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid image", e);
        }
    }

    @PutMapping(path = "/edit/{id}")
    public ResponseEntity<MealCategory> editCategory(@PathVariable Long id,
                                                     @RequestPart("image") MultipartFile image,
                                                     @RequestPart("meal_category") MealCategoryDto mealCategoryDto) {
        try {
            return ResponseEntity.ok(mealCategoryService.editCategory(mealCategoryDto, id, image));
        } catch (EntityExistsException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "There is no category with id " + id, e);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid image", e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MealCategory> deleteCategory(@PathVariable Long id) {
        try {
            mealCategoryService.deleteCategory(id);
            return ResponseEntity.ok().build();
        } catch (InvalidEntityException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
