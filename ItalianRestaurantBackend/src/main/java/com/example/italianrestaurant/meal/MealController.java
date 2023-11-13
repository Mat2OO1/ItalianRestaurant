package com.example.italianrestaurant.meal;

import com.example.italianrestaurant.meal.mealcategory.MealCategoryDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealService mealService;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<Page<Meal>> getAllMeals(Pageable pageable) {
        return ResponseEntity.ok(mealService.getAllMeals(pageable));
    }

    @GetMapping("/filter/{category}")
    public ResponseEntity<Page<Meal>> getAllMeals(@PathVariable String category, Pageable pageable) {
        return ResponseEntity.ok(mealService.getMealsByCategory(pageable, category));
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
    public ResponseEntity<Meal> addMeal(@RequestPart("image") MultipartFile image,
                                        @RequestPart("meal") String mealJson) throws JsonProcessingException {
        MealDto mealDto = objectMapper.readValue(mealJson, MealDto.class);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(mealService.addMeal(mealDto, image));
        } catch (EntityExistsException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "There already exists meal with name: " + mealDto.getName(), e
            );
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "There isn't a category with name " + mealDto.getCategory(), e
            );
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Image service not working", e
            );
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Meal> editMeal(@PathVariable Long id,
                                         @RequestPart(name = "image", required = false) MultipartFile image,
                                         @RequestPart("meal") String mealJson) throws JsonProcessingException {
        MealDto mealDto = objectMapper.readValue(mealJson, MealDto.class);
        try{
            return ResponseEntity.ok(mealService.editMeal(mealDto, id, image));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "There isn't a meal with id " + id, e
            );
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Image service not working", e
            );
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Meal> delete(@PathVariable Long id) {
        try{
            mealService.deleteMeal(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "There isn't a meal with id " + id, e
            );
        }
        return ResponseEntity.ok().build();
    }
}
