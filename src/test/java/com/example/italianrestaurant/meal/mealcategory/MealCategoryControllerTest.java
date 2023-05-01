package com.example.italianrestaurant.meal.mealcategory;

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

@WebMvcTest(controllers = MealCategoryController.class,
        excludeFilters =
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
public class MealCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MealCategoryService mealCategoryService;

    @Test
    void shouldGetAllMealCategories() throws Exception {
        // given
        val mealCategory = Utils.getMealCategory();
        mealCategory.setId(1L);
        val mealcategory2 = Utils.getMealCategory();
        mealcategory2.setId(2L);
        mealcategory2.setName("Pasta");
        given(mealCategoryService.getAllMealCategories()).willReturn(List.of(mealCategory, mealcategory2));

        // when
        val resultActions = mockMvc.perform(get("/meal-categories")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        List<MealCategory> mealCategoryList = Utils.jsonStringToObject(contentAsString, List.class);
        assertThat(mealCategoryList).hasSize(2);
    }

    @Test
    void shouldNotGetAllMealCategories() throws Exception {
        // given
        given(mealCategoryService.getAllMealCategories()).willReturn(List.of());

        // when
        val resultActions = mockMvc.perform(get("/meal-categories")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        List<MealCategory> mealCategoryList = Utils.jsonStringToObject(contentAsString, List.class);
        assertThat(mealCategoryList).hasSize(0);
    }

    @Test
    void shouldGetMealCategoryById() throws Exception {
        // given
        val mealCategory = Utils.getMealCategory();
        mealCategory.setId(1L);
        val mealcategory2 = Utils.getMealCategory();
        mealcategory2.setId(2L);
        mealcategory2.setName("Pasta");
        given(mealCategoryService.getMealById(mealCategory.getId())).willReturn(mealCategory);

        // when
        val resultActions = mockMvc.perform(get("/meal-categories/" + mealCategory.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        val result = Utils.jsonStringToObject(contentAsString, MealCategory.class);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void shouldNotGetMealCategoryById() throws Exception {
        // given
        val mealCategory = Utils.getMealCategory();
        mealCategory.setId(1L);
        val badId = 2L;

        given(mealCategoryService.getMealById(badId)).willThrow(EntityNotFoundException.class);

        // when
        val resultActions = mockMvc.perform(get("/meal-categories/" + badId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isNotFound());
    }
}
