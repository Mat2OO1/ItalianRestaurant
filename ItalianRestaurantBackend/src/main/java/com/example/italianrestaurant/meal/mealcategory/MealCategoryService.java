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

    public MealCategory addCategory(MealCategoryDto mealCategoryDto, MultipartFile image) throws IOException {
        if (mealCategoryRepository.findByName(mealCategoryDto.getName()).isEmpty()) {
            String imgName = awsService.uploadFile(image.getBytes(), image.getContentType());
            MealCategory mealCategory = modelMapper.map(mealCategoryDto, MealCategory.class);
            mealCategory.setImage(awsService.getObjectUrl(imgName));
            return mealCategoryRepository.save(mealCategory);
        }
        throw new EntityExistsException();
    }

    public MealCategory editCategory(MealCategoryDto mealCategoryDto, Long id, MultipartFile image) throws IOException {
        MealCategory mealCategory = mealCategoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        MealCategory mappedCategory = modelMapper.map(mealCategoryDto, MealCategory.class);

        if (image == null) {
            mappedCategory.setImage(mealCategory.getImage());
        }
        else {
            if (mealCategory.getImage() != null) awsService.deleteFile(mealCategory.getImage());
            String imgName = awsService.uploadFile(image.getBytes(), image.getContentType());
            mappedCategory.setImage(awsService.getObjectUrl(imgName));
        }
        mealCategory.setName(mealCategoryDto.getName());
        mappedCategory.setId(mealCategory.getId());
        return mealCategoryRepository.save(mappedCategory);
    }

    public void deleteCategory(Long id){
        MealCategory mealCategory = mealCategoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (mealCategory.getImage() != null) awsService.deleteFile(mealCategory.getImage());
        List<Meal> meals = mealCategory.getMeals();
        mealCategoryRepository.delete(mealCategory);
        meals.forEach(meal -> awsService.deleteFile(meal.getImage()));
    }

}
