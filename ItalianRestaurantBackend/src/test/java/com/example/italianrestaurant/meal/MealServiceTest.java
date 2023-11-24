package com.example.italianrestaurant.meal;

import com.example.italianrestaurant.Utils;
import com.example.italianrestaurant.aws.AwsService;
import com.example.italianrestaurant.meal.mealcategory.MealCategory;
import com.example.italianrestaurant.meal.mealcategory.MealCategoryService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MealServiceTest {
    @Mock
    private MealRepository mealRepository;
    @Mock
    private MealCategoryService mealCategoryService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private AwsService awsService;
    @InjectMocks
    private MealService mealService;


    @Test
    void shouldGetAllMeals() {
        //given
        val meal = Utils.getMealWithCategory();
        meal.setId(1L);
        val meal2 = Utils.getMealWithCategory();
        meal2.setId(2L);
        meal2.setName("Meal2");
        given(mealRepository.findAll(Pageable.unpaged())).willReturn(new PageImpl<>(List.of(meal, meal2), Pageable.unpaged(), 2));
        //when
        val returnedMeals = mealService.getAllMealsPaginated(Pageable.unpaged());

        //then
        assertThat(returnedMeals.getContent()).containsExactly(meal, meal2);
    }

    @Test
    void shouldNotGetAllMeals() {
        //given
        given(mealRepository.findAll(Pageable.unpaged())).willReturn(new PageImpl<>(List.of(), Pageable.unpaged(), 0));

        //when
        val returnedMeals = mealService.getAllMealsPaginated(Pageable.unpaged());

        //then
        assertThat(returnedMeals.getContent()).isEmpty();
    }

    @Test
    void shouldGetMealById() {
        //given
        val meal = Utils.getMealWithCategory();
        meal.setId(1L);
        given(mealRepository.findById(meal.getId())).willReturn(Optional.of(meal));
        //when
        val returnedMeal = mealService.getMealById(1L);

        //then
        assertThat(returnedMeal).isEqualTo(meal);
    }

    @Test
    void shouldNotGetMealById() {
        //given
        given(mealRepository.findById(1L)).willReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> mealService.getMealById(1L))
                .isInstanceOf(EntityNotFoundException.class);

        //then
        verify(mealRepository, times(0)).save(any());
    }

    @Test
    void shouldAddMeal() throws IOException {
        //given
        MealDto mealDto = Utils.getMealDto();
        MultipartFile image = new MockMultipartFile("name", new byte[1]);
        MealCategory mealCategory = Utils.getMealCategory();

        Meal mappedMeal = Utils.getMeal();
        mappedMeal.setMealCategory(mealCategory);

        Meal dbMeal = Utils.getMeal();
        dbMeal.setMealCategory(mealCategory);
        dbMeal.setId(1L);

        given(modelMapper.map(mealDto, Meal.class)).willReturn(mappedMeal);
        given(mealCategoryService.getMealCategoryByName(any())).willReturn(mealCategory);
        given(mealRepository.save(mappedMeal)).willReturn(dbMeal);
        given(awsService.uploadFile(any(), any())).willReturn("url");

        //when
        val returnedMeal = mealService.addMeal(mealDto, image);

        //then
        verify(mealRepository).save(mappedMeal);
        assertThat(returnedMeal).isEqualTo(dbMeal);
    }

    @Test
    void shouldNotAddMealWhenNameAlreadyExists() {
        //given
        MealDto mealDto = Utils.getMealDto();
        given(mealRepository.existsByName(any())).willReturn(true);

        //when
        assertThatThrownBy(() -> mealService.addMeal(mealDto, any())).isInstanceOf(EntityExistsException.class);

        //then
        verify(mealRepository, times(0)).save(any());
    }

    @Test
    void shouldNotAddMealWhenCategoryDoesntExist() {
        //given
        MealDto mealDto = Utils.getMealDto();
        given(mealRepository.existsByName(any())).willReturn(false);
        given(mealCategoryService.getMealCategoryByName(any())).willThrow(EntityNotFoundException.class);

        //when
        assertThatThrownBy(() -> mealService.addMeal(mealDto, any())).isInstanceOf(EntityNotFoundException.class);

        //then
        verify(mealRepository, times(0)).save(any());
    }

    @Test
    void shouldEditMeal() throws IOException {
        //given
        Meal dbMeal = Utils.getMealWithCategory();
        dbMeal.setId(1L);
        Meal mappedMeal = Utils.getMeal();
        MealDto mealDto = Utils.getMealDto();
        MealCategory mealCategory = Utils.getMealCategory();
        MultipartFile image = new MockMultipartFile("name", new byte[1]);
        given(mealRepository.findById(any())).willReturn(Optional.of(dbMeal));
        given(modelMapper.map(mealDto, Meal.class)).willReturn(mappedMeal);
        given(mealCategoryService.getMealCategoryByName(any())).willReturn(mealCategory);
        given(mealRepository.save(any())).willReturn(dbMeal);
        given(awsService.uploadFile(any(), any())).willReturn("url");
        //when
        val editedMeal = mealService.editMeal(mealDto, 1L, image);

        //then
        assertThat(editedMeal).isEqualTo(dbMeal);
        verify(mealRepository).save(mappedMeal);
    }

    @Test
    void shouldNotEditMealWhenMealWithGivenIdDoesntExist() {
        //given
        MealDto mealDto = Utils.getMealDto();
        given(mealRepository.findById(any())).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> mealService.editMeal(mealDto, 1L, any())).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldDeleteMeal() {
        Meal mealDb = Utils.getMeal();
        mealDb.setId(1L);

        given(mealRepository.findById(any())).willReturn(Optional.of(mealDb));
        //when
        mealService.deleteMeal(1L);
        //then
        verify(mealRepository, times(1)).delete(any());

    }

    @Test
    void shouldNotDeleteMealWhenMealDoesntExist() {
        given(mealRepository.findById(any())).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> mealService.deleteMeal(1L)).isInstanceOf(EntityNotFoundException.class);
    }


}
