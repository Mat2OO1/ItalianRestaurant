package com.example.italianrestaurant.meal;

import com.example.italianrestaurant.aws.AwsService;
import com.example.italianrestaurant.meal.mealcategory.MealCategory;
import com.example.italianrestaurant.meal.mealcategory.MealCategoryService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class MealService {

    private final MealRepository mealRepository;
    private final ModelMapper modelMapper;
    private final MealCategoryService mealCategoryService;
    private final AwsService awsService;

    public Page<Meal> getAllMealsPaginated(Pageable pageable) {
        return mealRepository.findAll(pageable);
    }

    public List<Meal> getAllMeals() {
        return mealRepository.getAllByDeletedFalse();
    }

    public Page<Meal> getMealsByCategory(Pageable pageable, String category){
        MealCategory mealCategory = mealCategoryService.getMealCategoryByName(category);
        if(mealCategory != null){
            log.info(mealRepository.getMealsByMealCategoryName(pageable, mealCategory.getName()));
            return mealRepository.getMealsByMealCategoryName(pageable, mealCategory.getName());
        }
        else {
            throw new EntityNotFoundException();
        }
    }
    public Meal getMealById(Long id) {
        return mealRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Meal addMeal(MealDto meal, MultipartFile image) throws IOException {
        if (mealRepository.existsByName(meal.getName())) {
            throw new EntityExistsException("Meal with name: " + meal.getName() + " already exists");
        }
        MealCategory category = mealCategoryService.getMealCategoryByName(meal.getCategory());
        String imgName = awsService.uploadFile(image.getBytes(), image.getContentType());
        Meal mappedMeal = modelMapper.map(meal, Meal.class);
        mappedMeal.setMealCategory(category);
        mappedMeal.setImage(awsService.getObjectUrl(imgName));
        return mealRepository.save(mappedMeal);
    }

    public Meal editMeal(MealDto mealDto, Long id, MultipartFile image) throws IOException {
        Meal savedMeal = mealRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        MealCategory mealCategory = mealCategoryService.getMealCategoryByName(mealDto.getCategory());
        Meal mappedMeal = modelMapper.map(mealDto, Meal.class);
        mappedMeal.setMealCategory(mealCategory);
        mappedMeal.setId(savedMeal.getId());
        if (image == null) {
            mappedMeal.setImage(savedMeal.getImage());
        } else {
            if (savedMeal.getImage() != null) awsService.deleteFile(savedMeal.getImage());
            String imgName = awsService.uploadFile(image.getBytes(), image.getContentType());
            mappedMeal.setImage(awsService.getObjectUrl(imgName));
        }
        return mealRepository.save(mappedMeal);
    }

    public void deleteMeal(long id){
        Meal meal = mealRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        log.info(meal);
        meal.setDeleted(true);
        mealRepository.save(meal);
    }

}
