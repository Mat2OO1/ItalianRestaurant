package com.example.italianrestaurant.meal;

import com.example.italianrestaurant.Utils;
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
public class MealServiceTest {
    @Mock
    private MealRepository mealRepository;

    @InjectMocks
    private MealService mealService;

    @Test
    void shouldGetAllMeals() {
        //given
        val meal = Utils.getMeal();
        meal.setId(1L);
        val meal2 = Utils.getMeal();
        meal2.setId(2L);
        meal2.setName("Meal2");

        given(mealRepository.findAll()).willReturn(List.of(meal, meal2));

        //when
        val returnedMeals = mealService.getAllMeals();

        //then
        assertThat(returnedMeals).containsExactly(meal, meal2);
    }

    @Test
    void shouldNotGetAllMeals() {
        //given
        given(mealRepository.findAll()).willReturn(List.of());

        //when
        val returnedMeals = mealService.getAllMeals();

        //then
        assertThat(returnedMeals).isEmpty();
    }

    @Test
    void shouldGetMealById() {
        //given
        val meal = Utils.getMeal();
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
}
