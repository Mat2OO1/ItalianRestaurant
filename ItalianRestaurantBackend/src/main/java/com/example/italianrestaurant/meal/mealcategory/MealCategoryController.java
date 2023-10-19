package com.example.italianrestaurant.meal.mealcategory;

import com.example.italianrestaurant.exceptions.InvalidEntityException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
public class MealCategoryController {

    private final MealCategoryService mealCategoryService;
    private final ObjectMapper objectMapper;

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
                                                    @RequestPart("meal_category") String mealCategoryJson) throws JsonProcessingException {
        MealCategoryDto mealCategoryDto = objectMapper.readValue(mealCategoryJson, MealCategoryDto.class);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(mealCategoryService.addCategory(mealCategoryDto, image));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "There is already a category with name " + mealCategoryDto.getName(), e);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid image", e);
        }
    }

    @PutMapping(path = "/edit/{id}")
    public ResponseEntity<MealCategory> editCategory(@PathVariable Long id,
                                                     @RequestPart("image") MultipartFile image,
                                                     @RequestPart("meal_category") String mealCategoryJson) throws JsonProcessingException {
        MealCategoryDto mealCategoryDto = objectMapper.readValue(mealCategoryJson, MealCategoryDto.class);
        try {
            return ResponseEntity.ok(mealCategoryService.editCategory(mealCategoryDto, id, image));
        } catch (EntityNotFoundException e) {
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
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
