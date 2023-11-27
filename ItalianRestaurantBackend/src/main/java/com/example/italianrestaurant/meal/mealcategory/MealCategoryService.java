package com.example.italianrestaurant.meal.mealcategory;

import com.example.italianrestaurant.aws.AwsService;
import com.example.italianrestaurant.meal.Meal;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MealCategoryService {

    private final MealCategoryRepository mealCategoryRepository;
    private final ModelMapper modelMapper;
    private final AwsService awsService;

    public List<MealCategory> getAllMealCategories() {
        return mealCategoryRepository.getMealCategoriesByDeletedIsFalse();
    }

    public MealCategory getMealCategoryById(Long id) {
        Optional<MealCategory> meal = mealCategoryRepository.findByIdAndDeletedIsFalse(id);
        return meal.orElseThrow(EntityNotFoundException::new);
    }

    public MealCategory getMealCategoryByName(String name) throws EntityNotFoundException {
        Optional<MealCategory> mealCategory = mealCategoryRepository.findByNameAndDeletedIsFalse(name);
        return mealCategory.orElseThrow(EntityNotFoundException::new);
    }

    public MealCategory addCategory(MealCategoryDto mealCategoryDto) throws IOException {
        if (mealCategoryRepository.findByNameAndDeletedIsFalse(mealCategoryDto.getName()).isEmpty()) {
            MealCategory mealCategory = modelMapper.map(mealCategoryDto, MealCategory.class);
            return mealCategoryRepository.save(mealCategory);
        }
        throw new EntityExistsException();
    }

    public MealCategory editCategory(MealCategoryDto mealCategoryDto, Long id) throws IOException {
        MealCategory mealCategory = mealCategoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        MealCategory mappedCategory = modelMapper.map(mealCategoryDto, MealCategory.class);
        mealCategory.setName(mealCategoryDto.getName());
        mappedCategory.setId(mealCategory.getId());
        return mealCategoryRepository.save(mappedCategory);
    }

    public void deleteCategory(Long id){
        MealCategory mealCategory = mealCategoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        List<Meal> meals = mealCategory.getMeals();
        meals.forEach(meal -> meal.setDeleted(true));
        mealCategory.setDeleted(true);
        meals.forEach(meal -> awsService.deleteFile(meal.getImage()));
        mealCategoryRepository.save(mealCategory);
    }

}
