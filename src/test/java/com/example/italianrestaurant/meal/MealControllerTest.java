package com.example.italianrestaurant.meal;

import com.example.italianrestaurant.Utils;
import com.example.italianrestaurant.config.security.JwtAuthenticationFilter;
import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = MealController.class,
        excludeFilters =
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
public class MealControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MealService mealService;

    @Test
    void shouldGetAllMeals() throws Exception {
        // given
        val meal = Utils.getMeal();
        meal.setId(1L);
        val meal2 = Utils.getMeal();
        meal2.setId(2L);
        meal2.setName("Pasta");
        given(mealService.getAllMeals()).willReturn(List.of(meal, meal2));

        // when
        val resultActions = mockMvc.perform(get("/meals")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        List<Meal> mealList = Utils.jsonStringToObject(contentAsString, List.class);
        assertThat(mealList).hasSize(2);
    }

    @Test
    void shouldNotGetAllMeals() throws Exception {
        // given
        given(mealService.getAllMeals()).willReturn(List.of());

        // when
        val resultActions = mockMvc.perform(get("/meals")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        List<Meal> mealList = Utils.jsonStringToObject(contentAsString, List.class);
        assertThat(mealList).hasSize(0);
    }

    @Test
    void shouldGetMealById() throws Exception {
        // given
        val meal = Utils.getMeal();
        meal.setId(1L);
        val meal2 = Utils.getMeal();
        meal2.setId(2L);
        meal2.setName("Pasta");
        given(mealService.getMealById(meal.getId())).willReturn(meal);

        // when
        val resultActions = mockMvc.perform(get("/meals/" + meal.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        val result = Utils.jsonStringToObject(contentAsString, Meal.class);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void shouldNotGetMealById() throws Exception {
        // given
        val meal = Utils.getMeal();
        meal.setId(1L);
        val badId = 2L;

        given(mealService.getMealById(badId)).willThrow(EntityNotFoundException.class);

        // when
        val resultActions = mockMvc.perform(get("/meals/" + badId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isNotFound());
    }
}
