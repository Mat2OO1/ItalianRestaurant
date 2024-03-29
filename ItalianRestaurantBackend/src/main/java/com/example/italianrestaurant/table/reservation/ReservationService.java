package com.example.italianrestaurant.table.reservation;

import com.example.italianrestaurant.security.UserPrincipal;
import com.example.italianrestaurant.table.Table;
import com.example.italianrestaurant.user.User;
import com.example.italianrestaurant.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public Reservation addReservation(UserPrincipal userPrincipal, ReservationDto reservationDto) {
        User user = userService.getUserByEmail(userPrincipal.getEmail());
        if (this.reservationRepository.getReservationsByUserAndDate(userPrincipal.getEmail(), reservationDto.getReservationDateStart()).size() != 0) {
            throw new UserReservationConflictException();
        } else if (this.reservationRepository.getReservedTables(
                        reservationDto.getReservationDateStart(),
                        reservationDto.getReservationDateStart().plusMinutes(59))
                .stream().anyMatch(reservation -> Objects.equals(reservation.getTable().getId(), reservationDto.getTable().getId()))) {
            throw new TableAlreadyReservedException();
        }
        Reservation reservation = modelMapper.map(reservationDto, Reservation.class);
        reservation.setUser(user);
        return reservationRepository.save(reservation);

    }

    public List<Reservation> getReservations(UserPrincipal userPrincipal) {
        return this.reservationRepository.getAllByUserEmailAndDateAfter(userPrincipal.getEmail(), LocalDate.now());
    }

    public List<Reservation> getReservationsForDate(LocalDate date) {
        return this.reservationRepository.getReservationByDate(date);
    }

    public void deleteReservation(Long id) {
        if (this.reservationRepository.existsById(id)) {
            this.reservationRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Table with id: " + id + " doesn't exist");
        }
    }

    public List<Table> getReservedTables() {
        LocalDateTime timeFrom = LocalDateTime.now().minusHours(1);
        LocalDateTime timeTo = LocalDateTime.now().plusHours(1);
        //table is reserved one hour before reservation starts, and the reservation lasts one hour
        return this.reservationRepository.getReservedTables(timeFrom, timeTo)
                .stream().map(Reservation::getTable)
                .collect(Collectors.toList());
    }

    public List<LocalDateTime> getReservationsForTable(Long tableId, LocalDate date) {
        return this.reservationRepository.getReservationForTable(tableId, date)
                .stream().map(Reservation::getReservationDateStart)
                .collect(Collectors.toList());
    }

}
