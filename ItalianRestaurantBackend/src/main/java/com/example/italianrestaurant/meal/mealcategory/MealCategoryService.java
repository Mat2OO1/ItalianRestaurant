package com.example.italianrestaurant.meal.mealcategory;

import com.example.italianrestaurant.exceptions.InvalidEntityException;
import com.example.italianrestaurant.meal.MealService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MealCategoryService {

    private final MealCategoryRepository mealCategoryRepository;
    private final ModelMapper modelMapper;

    public List<MealCategory> getAllMealCategories() {
        return mealCategoryRepository.findAll();
    }

    public MealCategory getMealCategoryById(Long id) {
        Optional<MealCategory> meal = mealCategoryRepository.findById(id);
        return meal.orElseThrow(EntityNotFoundException::new);
    }

    public MealCategory getMealCategoryByName(String name) throws EntityNotFoundException {
        Optional<MealCategory> mealCategory = mealCategoryRepository.findByName(name);
        return mealCategory.orElseThrow(EntityNotFoundException::new);
    }

    public MealCategory addCategory(MealCategoryDto mealCategoryDto) {
        if (mealCategoryRepository.findByName(mealCategoryDto.getName()).isEmpty()) {
            MealCategory mealCategory = modelMapper.map(mealCategoryDto, MealCategory.class);
            return mealCategoryRepository.save(mealCategory);
        }
        throw new EntityExistsException();
    }

    public MealCategory editCategory(MealCategoryDto mealCategoryDto, Long id) {
        MealCategory mealCategory = mealCategoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        MealCategory mappedCategory = modelMapper.map(mealCategoryDto, MealCategory.class);
        mappedCategory.setId(mealCategory.getId());
        return mealCategoryRepository.save(mappedCategory);
    }

    public void deleteCategory(Long id){
        MealCategory mealCategory = mealCategoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        mealCategoryRepository.delete(mealCategory);
    }

}
