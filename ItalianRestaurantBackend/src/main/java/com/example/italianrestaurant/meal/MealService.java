package com.example.italianrestaurant.meal;

import com.example.italianrestaurant.meal.mealcategory.MealCategory;
import com.example.italianrestaurant.meal.mealcategory.MealCategoryService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;

    private final ModelMapper modelMapper;

    private final MealCategoryService mealCategoryService;

    public Page<Meal> getAllMeals(Pageable pageable) {
        return mealRepository.findAll(pageable);
    }

    public Meal getMealById(Long id) {
        Optional<Meal> meal = mealRepository.findById(id);

        return meal.orElseThrow(EntityNotFoundException::new);
    }

    public Meal addMeal(MealDto meal){
        if(mealRepository.existsByName(meal.getName())){
            throw new EntityExistsException("Meal with name: " + meal.getName() + " already exists");
        }
        MealCategory category = mealCategoryService.getMealCategoryByName(meal.getCategory());
        Meal mappedMeal = modelMapper.map(meal, Meal.class);
        mappedMeal.setMealCategory(category);
        return mealRepository.save(mappedMeal);

    }
}
