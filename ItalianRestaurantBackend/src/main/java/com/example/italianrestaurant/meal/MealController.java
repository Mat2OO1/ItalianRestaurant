package com.example.italianrestaurant.meal;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;

    @GetMapping
    public ResponseEntity<Page<Meal>> getAllMeals(Pageable pageable) {
        return ResponseEntity.ok(mealService.getAllMeals(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Meal> getMealById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(mealService.getMealById(id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "There is no meal with id: " + id, e);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Meal> addMeal(@RequestBody MealDto mealDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(mealService.addMeal(mealDto));
        } catch (EntityExistsException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "There already exists meal with name: " + mealDto.getName(), e
            );
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "There isn't a category with name " + mealDto.getCategory(), e
            );
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Meal> editMeal(@RequestBody MealDto mealDto, @PathVariable Long id) {
        try{
            return ResponseEntity.ok(mealService.editMeal(mealDto, id));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "There isn't a meal with id " + id, e
            );
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Meal> delete(@PathVariable Long id) {
        try{
            mealService.deleteMeal(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "There isn't a meal with id " + id, e
            );
        }
        return ResponseEntity.ok().build();
    }
}
