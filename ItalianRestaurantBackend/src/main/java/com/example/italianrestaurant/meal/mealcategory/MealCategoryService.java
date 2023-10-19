package com.example.italianrestaurant.meal.mealcategory;

import com.example.italianrestaurant.aws.AwsService;
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
        List<MealCategory> mealCategories = mealCategoryRepository.findAll();
        mealCategories.forEach(mealCategory -> mealCategory.setImage(awsService.getObjectUrl(mealCategory.getImage())));
        return mealCategories;
    }

    public MealCategory getMealCategoryById(Long id) {
        Optional<MealCategory> meal = mealCategoryRepository.findById(id);
        MealCategory mealCategory = meal.orElseThrow(EntityNotFoundException::new);
        mealCategory.setImage(awsService.getObjectUrl(mealCategory.getImage()));
        return mealCategory;
    }

    public MealCategory getMealCategoryByName(String name) throws EntityNotFoundException {
        Optional<MealCategory> mealCategory = mealCategoryRepository.findByName(name);
        MealCategory savedMealCategory = mealCategory.orElseThrow(EntityNotFoundException::new);
        savedMealCategory.setImage(awsService.getObjectUrl(savedMealCategory.getImage()));
        return savedMealCategory;
    }

    public MealCategory addCategory(MealCategoryDto mealCategoryDto, MultipartFile image) throws IOException {
        if (mealCategoryRepository.findByName(mealCategoryDto.getName()).isEmpty()) {
            String imgName = awsService.uploadFile(image.getBytes(), image.getContentType());
            MealCategory mealCategory = modelMapper.map(mealCategoryDto, MealCategory.class);
            mealCategory.setImage(imgName);
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
            mappedCategory.setImage(imgName);
        }
        mealCategory.setName(mealCategoryDto.getName());
        mappedCategory.setId(mealCategory.getId());
        return mealCategoryRepository.save(mappedCategory);
    }

    public void deleteCategory(Long id){
        MealCategory mealCategory = mealCategoryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if (mealCategory.getImage() != null) awsService.deleteFile(mealCategory.getImage());
        mealCategoryRepository.delete(mealCategory);
    }

}
