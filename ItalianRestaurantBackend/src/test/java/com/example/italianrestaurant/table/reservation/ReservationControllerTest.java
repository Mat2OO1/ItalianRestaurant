package com.example.italianrestaurant.table.reservation;

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
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = ReservationController.class,
        excludeFilters =
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)

public class ReservationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReservationService reservationService;

    @Test
    void shouldSaveReservation() throws Exception {
        //given
        val reservation = Utils.getReservation();
        val reservationDto = Utils.getReservationDto();
        given(reservationService.addReservation(any(), any())).willReturn(reservation);

        //when
        val resultActions = mockMvc.perform(post("/reservations/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.objectToJsonString(reservationDto)));

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void shouldNotSaveReservationWhenUserHaveReservationForThisDay() throws Exception {
        //given
        val reservationDto = Utils.getReservationDto();
        given(reservationService.addReservation(any(), any())).willThrow(UserReservationConflictException.class);

        //when
        val resultActions = mockMvc.perform(post("/reservations/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.objectToJsonString(reservationDto)));

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void shouldNotSaveReservationWhenTableAlreadyReserved() throws Exception {
        //given
        val reservationDto = Utils.getReservationDto();
        given(reservationService.addReservation(any(), any())).willThrow(TableAlreadyReservedException.class);

        //when
        val resultActions = mockMvc.perform(post("/reservations/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.objectToJsonString(reservationDto)));

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteReservation() throws Exception {
        //given
        val reservation = Utils.getReservation();

        //when
        val resultActions = mockMvc.perform(delete("/reservations/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                        .param("reservationId", Utils.objectToJsonString(reservation.getId())));

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void shouldNotDeleteWhenReservationDoesntExist() throws Exception {
        //given
        val reservation = Utils.getReservation();
        doThrow(EntityNotFoundException.class).when(reservationService).deleteReservation(any());

        //when
        val resultActions = mockMvc.perform(delete("/reservations/cancel")
                .contentType(MediaType.APPLICATION_JSON)
                .param("reservationId", Utils.objectToJsonString(reservation.getId())));

        //then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUserReservations() throws Exception {
        //given
        val reservation = Utils.getReservation();
        given(reservationService.getReservations(any())).willReturn(List.of(reservation));

        //when
        val resultActions = mockMvc.perform(get("/reservations/user")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(reservation.getId()))
                .andExpect(jsonPath("$[0].reservationDateStart").exists())
                .andExpect(jsonPath("$[0].table").exists())
                .andExpect(jsonPath("$[0].user").exists());
    }

    @Test
    void shouldReturnReservedTables() throws Exception {
        //given
        val table = Utils.getTable();
        given(reservationService.getReservedTables()).willReturn(List.of(table));

        //when
        val resultActions = mockMvc.perform(get("/reservations/reserved/all")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].number").value(table.getNumber()))
                .andExpect(jsonPath("$[0].seats").value(table.getSeats()));
    }

    @Test
    void shouldReturnReservationsForTable() throws Exception {
        //given
        val date = LocalDate.of(2024,1,1);
        val dateTime = LocalDateTime.of(2024,1,1,1,1,1);

        val table = Utils.getTable();
        table.setId(1L);
        given(reservationService.getReservationsForTable(anyLong(),any(LocalDate.class))).willReturn(List.of(dateTime));

        //when
        val resultActions = mockMvc.perform(get("/reservations/reserved")
                .contentType(MediaType.APPLICATION_JSON)
                .param("tableId", Utils.objectToJsonString(table.getId()))
                .param("date", date.toString()));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }





}
