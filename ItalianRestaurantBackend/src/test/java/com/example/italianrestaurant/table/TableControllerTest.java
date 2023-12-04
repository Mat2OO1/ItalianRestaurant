package com.example.italianrestaurant.table;

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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TableController.class,
        excludeFilters =
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
public class TableControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TableService tableService;

    @Test
    void shouldGetAllTables() throws Exception {
        //given
        val table = Utils.getTable();
        given(tableService.getAllNotDeletedTables()).willReturn(List.of(table));

        // when
        val resultActions = mockMvc.perform(get("/tables")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].number").value(table.getNumber()));
    }

    @Test
    void shouldGetTableByNumber() throws Exception {
        //given
        val number = 1L;
        val table = Utils.getTable();
        table.setNumber(1);
        given(tableService.getTableByNumber(1L)).willReturn(table);

        // when
        val resultActions = mockMvc.perform(get("/tables/" + number)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(number))
                .andExpect(jsonPath("$.number").value(table.getNumber()));
    }

    @Test
    void shouldNotGetTableByNumber() throws Exception {
        //given
        val number = 1L;
        given(tableService.getTableByNumber(1L)).willThrow(new EntityNotFoundException());

        // when
        val resultActions = mockMvc.perform(get("/tables/" + number)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void shouldSaveTable() throws Exception {
        //given
        val table = Utils.getTable();
        val tableDto = Utils.getTableDto();
        given(tableService.saveTable(any())).willReturn(table);

        //when
        val resultActions = mockMvc.perform(post("/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.objectToJsonString(tableDto)));

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void shouldNotSaveTable() throws Exception {
        //given
        val tableDto = new TableDto();

        //when
        val resultActions = mockMvc.perform(post("/tables")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.objectToJsonString(tableDto)));

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteTable() throws Exception {
        //given
        val id = 1L;
        doNothing().when(tableService).deleteTable(id);

        //when
        val resultActions = mockMvc.perform(delete("/tables/" + id)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void shouldNotDeleteTable() throws Exception {
        //given
        val id = 1L;
        doThrow(new EmptyResultDataAccessException(0)).when(tableService).deleteTable(id);

        //when
        val resultActions = mockMvc.perform(delete("/tables/" + id)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isNotFound());
    }
}
