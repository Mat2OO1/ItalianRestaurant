package com.example.italianrestaurant.meal.mealcategory;

import com.example.italianrestaurant.Utils;
import com.example.italianrestaurant.security.JwtAuthenticationFilter;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        given(mealCategoryService.getMealCategoryById(mealCategory.getId())).willReturn(mealCategory);

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

        given(mealCategoryService.getMealCategoryById(badId)).willThrow(EntityNotFoundException.class);

        // when
        val resultActions = mockMvc.perform(get("/meal-categories/" + badId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void shouldAddMealCategory() throws Exception {
        //given
        val mealCategoryDto = Utils.getMealCategoryDto();
        val mealCategory = Utils.getMealCategory();
        val mealCategoryJson = Utils.objectToJsonString(mealCategoryDto);

        given(mealCategoryService.addCategory(mealCategoryDto)).willReturn(mealCategory);

        //when
        val resultActions = mockMvc.perform(post("/meal-categories/add")
                .content(mealCategoryJson)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    void shouldEditCategory() throws Exception {
        //given
        val mealCategoryDto = Utils.getMealCategoryDto();
        val mealCategory = Utils.getMealCategory();
        val id = 1L;

        val mealCategoryJson = Utils.objectToJsonString(mealCategoryDto);

        given(mealCategoryService.editCategory(mealCategoryDto, id)).willReturn(mealCategory);

        //when
        val resultActions = mockMvc.perform(put("/meal-categories/edit/" + id)
                .content(mealCategoryJson)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void shouldNotEditCategory() throws Exception {
        //given
        val mealCategoryDto = Utils.getMealCategoryDto();
        val mealCategoryJson = Utils.objectToJsonString(mealCategoryDto);
        val id = 1L;

        given(mealCategoryService.editCategory(any(), eq(id))).willThrow(EntityNotFoundException.class);

        //when
        val resultActions = mockMvc.perform(put("/meal-categories/edit/" + id)
                .content(mealCategoryJson)
                .contentType(MediaType.APPLICATION_JSON));
        //then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        //given
        val id = 1L;
        //when
        val resultActions = mockMvc.perform(delete("/meal-categories/delete/" + id)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void shouldNotDelete() throws Exception {
        //given
        val id = 1L;
        doThrow(new EntityNotFoundException()).when(mealCategoryService).deleteCategory(id);
        //when
        val resultActions = mockMvc.perform(delete("/meal-categories/delete/" + id)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isNotFound());
    }
}
