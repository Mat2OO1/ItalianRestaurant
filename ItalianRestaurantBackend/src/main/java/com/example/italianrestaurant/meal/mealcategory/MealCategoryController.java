package com.example.italianrestaurant.meal.mealcategory;

import com.example.italianrestaurant.exceptions.InvalidEntityException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @PostMapping("/add")
    public ResponseEntity<MealCategory> addCategory(@RequestBody MealCategoryDto mealCategoryDto){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(mealCategoryService.addCategory(mealCategoryDto));
        } catch (EntityExistsException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "There is already a category with name " + mealCategoryDto.getName(), e);
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<MealCategory> editCategory(@RequestBody MealCategoryDto mealCategoryDto, @PathVariable Long id){
        try{
            log.info(mealCategoryDto);
            log.info(id);
            return ResponseEntity.ok(mealCategoryService.editCategory(mealCategoryDto,id));
        } catch (EntityExistsException e){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "There is no category with id " + id, e);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MealCategory> deleteCategory(@PathVariable Long id){
        try{
            mealCategoryService.deleteCategory(id);
            return ResponseEntity.ok().build();
        } catch (InvalidEntityException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
