package com.example.italianrestaurant.meal.mealcategory;

import com.example.italianrestaurant.Utils;
import com.example.italianrestaurant.aws.AwsService;
import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MealCategoryServiceTest {

    @Mock
    private MealCategoryRepository mealCategoryRepository;
    @Mock
    private AwsService awsService;
    @InjectMocks
    private MealCategoryService mealCategoryService;

    @Test
    void shouldGetAllMealCategories() {
        //given
        val mealCategory = Utils.getMealCategory();
        mealCategory.setId(1L);
        val mealCategory2 = Utils.getMealCategory();
        mealCategory2.setId(2L);
        mealCategory2.setName("Meal2");

        given(mealCategoryRepository.findAll()).willReturn(List.of(mealCategory, mealCategory2));
        given(awsService.getObjectUrl(any())).willReturn("url");

        //when
        val returnedMealCategories = mealCategoryService.getAllMealCategories();

        //then
        assertThat(returnedMealCategories).containsExactly(mealCategory, mealCategory2);
    }

    @Test
    void shouldNotGetAllMealCategories() {
        //given
        given(mealCategoryRepository.findAll()).willReturn(List.of());

        //when
        val returnedMealCategories = mealCategoryService.getAllMealCategories();

        //then
        assertThat(returnedMealCategories).isEmpty();
    }

    @Test
    void shouldGetMealCategoryById() {
        //given
        val mealCategory = Utils.getMealCategory();
        mealCategory.setId(1L);
        given(mealCategoryRepository.findById(1L)).willReturn(Optional.of(mealCategory));
        given(awsService.getObjectUrl(any())).willReturn("url");
        //when
        val returnedMealCategory = mealCategoryService.getMealCategoryById(1L);

        //then
        assertThat(returnedMealCategory).isEqualTo(mealCategory);
    }

    @Test
    void shouldNotGetMealCategoryById() {
        //given
        given(mealCategoryRepository.findById(1L)).willReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> mealCategoryService.getMealCategoryById(1L))
                .isInstanceOf(EntityNotFoundException.class);

        //then
        verify(mealCategoryRepository, times(0)).save(any());
    }

    @Test
    void shouldGetMealCategoryByName(){
        //given
        MealCategory mealCategory = Utils.getMealCategory();
        given(mealCategoryRepository.findByName(mealCategory.getName())).willReturn(Optional.of(mealCategory));
        given(awsService.getObjectUrl(any())).willReturn("url");
        //when
        val returnedMealCategory = mealCategoryService.getMealCategoryByName(mealCategory.getName());

        //then
        assertThat(mealCategoryService.getMealCategoryByName(mealCategory.getName())).isEqualTo(returnedMealCategory);
    }

    @Test
    void shouldNotGetMealCategoryByName(){
        //given
        given(mealCategoryRepository.findByName(any())).willReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> mealCategoryService.getMealCategoryByName(any())).isInstanceOf(EntityNotFoundException.class);

        //then
        verify(mealCategoryRepository, times(1)).findByName(any());
    }



}
