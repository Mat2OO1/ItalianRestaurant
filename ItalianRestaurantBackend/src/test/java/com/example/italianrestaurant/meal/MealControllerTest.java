package com.example.italianrestaurant.meal;

import com.example.italianrestaurant.Utils;
import com.example.italianrestaurant.security.JwtAuthenticationFilter;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        ArrayList<Meal> mealList = new ArrayList<>(List.of(meal, meal2));
        Pageable pageable = PageRequest.of(0, 10);
        PageImpl<Meal> mockPage = new PageImpl<>(mealList, pageable, mealList.size());
        when(mealService.getAllMeals(pageable)).thenReturn(mockPage);

        // when then
        mockMvc.perform(get("/meals")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("name"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].name").value("Pasta"));
    }

    @Test
    void shouldGetAllMealsPaginated() throws Exception {
        // given
        val meal = Utils.getMeal();
        meal.setId(1L);
        val meal2 = Utils.getMeal();
        meal2.setId(2L);
        meal2.setName("Pasta");
        ArrayList<Meal> mealList = new ArrayList<>(List.of(meal, meal2, meal, meal));

        Pageable pageable = PageRequest.of(0, 1);
        PagedListHolder pagedListHolder = new PagedListHolder(mealList);
        pagedListHolder.setPage(pageable.getPageNumber());
        pagedListHolder.setPageSize(pageable.getPageSize());

        testPage(mealList, pagedListHolder, pageable);
        pagedListHolder.nextPage();
        testPage(mealList, pagedListHolder, pageable);
    }

    private void testPage(List<Meal> mealList, PagedListHolder pages, Pageable pageable) throws Exception {
        PageImpl mockPage = new PageImpl<>(pages.getPageList(), pageable, mealList.size());
        when(mealService.getAllMeals(pageable)).thenReturn(mockPage);
        // when then
        mockMvc.perform(get("/meals")
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1)) // Expecting one item per page
                .andExpect(jsonPath("$.content[0].id").value(mealList.get(pages.getPage()).getId()))
                .andExpect(jsonPath("$.content[0].name").value(mealList.get(pages.getPage()).getName()));
    }

    @Test
    void shouldNotGetAllMeals() throws Exception {
        // given

        Pageable pageable = Pageable.unpaged();
        PageImpl<Meal> mockPage = new PageImpl<>(List.of(), pageable, 0);

        given(mealService.getAllMeals(any())).willReturn(mockPage);

        // when
        mockMvc.perform(get("/meals")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
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

    @Test
    void shouldAddMeal() throws Exception {
        // given
        val mealDto = Utils.getMealDto();
        val dbMeal = Utils.getMeal();
        val mealCategory = Utils.getMealCategory();
        dbMeal.setMealCategory(mealCategory);
        dbMeal.setId(1L);
        mealDto.setCategory(mealCategory.getName());

        val mealJson = Utils.objectToJsonString(mealDto);
        val image = new MockMultipartFile("image", new byte[1]);

        given(mealService.addMeal(eq(mealDto), any())).willReturn(dbMeal);

        // when
        val resultActions = mockMvc.perform(multipart("/meals/add")
                .file("image", image.getBytes())
                .file("meal", mealJson.getBytes()));

        // then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    void shouldNotAddMealWhenMealWithTheSameNameExists() throws Exception {
        val mealDto = Utils.getMealDto();
        val mealCategory = Utils.getMealCategory();
        mealDto.setCategory(mealCategory.getName());
        given(mealService.addMeal(any(), any())).willThrow(EntityExistsException.class);

        val mealJson = Utils.objectToJsonString(mealDto);
        val image = new MockMultipartFile("image", new byte[1]);

        // when
        val resultActions = mockMvc.perform(multipart("/meals/add")
                .file("image", image.getBytes())
                .file("meal", mealJson.getBytes()));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotAddMealWhenCategoryDoesntExist() throws Exception {
        val mealDto = Utils.getMealDto();
        val mealCategory = Utils.getMealCategory();
        mealDto.setCategory(mealCategory.getName());
        given(mealService.addMeal(any(), any())).willThrow(EntityNotFoundException.class);
        val mealJson = Utils.objectToJsonString(mealDto);
        val image = new MockMultipartFile("image", new byte[1]);

        // when
        val resultActions = mockMvc.perform(multipart("/meals/add")
                .file("image", image.getBytes())
                .file("meal", mealJson.getBytes()));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void shouldEditMeal() throws Exception {
        //given
        val mealDto = Utils.getMealDto();
        val dbMeal = Utils.getMealWithCategory();
        dbMeal.setId(1L);
        mealDto.setName("new name");
        val mealJson = Utils.objectToJsonString(mealDto);
        val image = new MockMultipartFile("image", new byte[1]);
        given(mealService.editMeal(any(), any(), any())).willReturn(dbMeal);

        //when
        val resultActions = mockMvc.perform(Utils.multipartPutRequest("/meals/edit/" + 1L)
                .file("image", image.getBytes())
                .file("meal", mealJson.getBytes())
                .contentType(MediaType.MULTIPART_FORM_DATA));

        //then
        resultActions.andExpect(jsonPath("$.id").value(dbMeal.getId()))
                .andExpect(jsonPath("$.name").value(dbMeal.getName()))
                .andExpect(jsonPath("$.description").value(dbMeal.getDescription()))
                .andExpect(jsonPath("$.mealCategory").exists())
                .andExpect(jsonPath("$.price").value(dbMeal.getPrice()));
    }


    @Test
    void shouldNotEditMealWhenCategoryDoesntExist() throws Exception {
        //given
        MealDto mealDto = Utils.getMealDto();
        val mealJson = Utils.objectToJsonString(mealDto);
        val image = new MockMultipartFile("image", new byte[1]);
        given(mealService.editMeal(any(), any(), any())).willThrow(EntityNotFoundException.class);

        //when
        val resultActions = mockMvc.perform(Utils.multipartPutRequest("/meals/edit/" + 1L)
                .file("image", image.getBytes())
                .file("meal", mealJson.getBytes())
                .contentType(MediaType.MULTIPART_FORM_DATA));

        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteMeal() throws Exception {
        //given
        val id = 1L;

        // when
        val resultActions = mockMvc.perform(delete("/meals/delete/" + id));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void shouldNotDeleteMeal() throws Exception {
        //given
        val id = 1L;
        doThrow(EntityNotFoundException.class).when(mealService).deleteMeal(id);
        // when
        val resultActions = mockMvc.perform(delete("/meals/delete/" + id));

        // then
        resultActions.andExpect(status().isNotFound());
    }
}
