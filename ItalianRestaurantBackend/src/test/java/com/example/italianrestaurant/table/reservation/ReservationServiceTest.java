package com.example.italianrestaurant.table.reservation;

import com.example.italianrestaurant.Utils;
import com.example.italianrestaurant.table.Table;
import com.example.italianrestaurant.security.UserPrincipal;
import com.example.italianrestaurant.user.User;
import com.example.italianrestaurant.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void shouldAddReservation(){
        //given
        User user = Utils.getUser();
        ReservationDto reservationDto = Utils.getReservationDto();
        Reservation reservation = Utils.getReservation();
        given(reservationRepository.getReservationsByUserAndDate(any(), any())).willReturn(List.of());
        given(reservationRepository.getReservedTables(any(), any())).willReturn(List.of());
        given(reservationRepository.save(reservation)).willReturn(reservation);
        given(modelMapper.map(any(), eq(Reservation.class))).willReturn(reservation);
        given(userService.getUserByEmail(any())).willReturn(user);
        //when
        val savedReservation = reservationService.addReservation(UserPrincipal.create(user), reservationDto);
        //then
        assertThat(savedReservation).isEqualTo(reservation);
    }

    @Test
    void shouldNotAddReservationWhenUserAlreadyHasReservation() {
        // Given
        User user = Utils.getUser();
        ReservationDto reservationDto = Utils.getReservationDto();
        given(reservationRepository.getReservationsByUserAndDate(any(), any()))
                .willReturn(List.of(new Reservation())); // Existing reservations for the user

        // When and Then
        assertThatThrownBy(() -> reservationService.addReservation(UserPrincipal.create(user), reservationDto))
                .isInstanceOf(UserReservationConflictException.class);
    }

    @Test
    void shouldNotAddReservationWhenTableIsAlreadyReserved() {
        // Given
        User user = Utils.getUser();
        ReservationDto reservationDto = Utils.getReservationDto();
        given(reservationRepository.getReservationsByUserAndDate(any(), any())).willReturn(List.of()); // No existing reservations
        given(reservationRepository.getReservedTables(any(), any()))
                .willReturn(List.of(Utils.getReservation())); // Existing reservations for the table

        // When and Then
        assertThatThrownBy(() -> reservationService.addReservation(UserPrincipal.create(user), reservationDto))
                .isInstanceOf(TableAlreadyReservedException.class);
    }

    @Test
    void shouldGetReservations() {
        // Given
        UserPrincipal userPrincipal = UserPrincipal.create(Utils.getUser());
        List<Reservation> reservations = List.of(Utils.getReservation());
        given(reservationRepository.getAllByUserEmailAndDateAfter(any(), any()))
                .willReturn(reservations);

        // When
        List<Reservation> retrievedReservations = reservationService.getReservations(userPrincipal);

        // Then
        assertThat(retrievedReservations).isEqualTo(reservations);
    }

    @Test
    void shouldDeleteReservation() {
        // Given
        Long reservationId = 1L;
        given(reservationRepository.existsById(reservationId)).willReturn(true);

        // When
        reservationService.deleteReservation(reservationId);

        // Then
        verify(reservationRepository, times(1)).deleteById(reservationId);
    }

    @Test
    void shouldNotDeleteReservationWhenReservationDoesNotExist() {
        // Given
        Long reservationId = 1L;
        given(reservationRepository.existsById(reservationId)).willReturn(false);

        // When and Then
        assertThatThrownBy(() -> reservationService.deleteReservation(reservationId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Table with id: " + reservationId + " doesn't exist");
    }

    @Test
    void shouldGetReservedTables() {
        // Given
        List<Reservation> reservedTables = List.of(Utils.getReservation());
        given(reservationRepository.getReservedTables(any(), any())).willReturn(reservedTables);

        // When
        List<Table> reservedTableList = reservationService.getReservedTables();

        // Then
        assertThat(reservedTableList).isEqualTo(reservedTables.stream()
                .map(Reservation::getTable)
                .collect(Collectors.toList()));
    }

    @Test
    void shouldGetReservationTimeForTable() {
        // Given
        Reservation reservation = Utils.getReservation();
        List<Reservation> reservedTables = List.of(reservation);
        reservation.getTable().setId(1L);
        given(reservationRepository.getReservationForTable(any(long.class), any())).willReturn(reservedTables);

        // When
        List<LocalDateTime> reservedTableList = reservationService.getReservationsForTable(reservation.getTable().getId(), LocalDate.from(reservation.getReservationDateStart()));

        // Then
        assertThat(reservedTableList).isEqualTo(reservedTables
                .stream().map(Reservation::getReservationDateStart)
                .collect(Collectors.toList()));
    }



}

