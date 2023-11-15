package com.example.italianrestaurant.table.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("FROM Reservation r WHERE r.user.email = :email AND r.reservationDateStart > :date")
    List<Reservation> getAllByUserEmailAndDateAfter(String email, LocalDateTime date);

    @Query("FROM Reservation r WHERE (r.reservationDateStart > :timeFrom AND r.reservationDateStart < :timeTo)")
    List<Reservation> getReservedTables(LocalDateTime timeFrom, LocalDateTime timeTo);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.table.id = :tableId " +
            "AND DATE(r.reservationDateStart) = DATE(:date)")
    List<Reservation> getReservationForTable(Long tableId, LocalDate date);

    @Query("FROM Reservation r WHERE DATE(r.reservationDateStart) = DATE(:date) AND r.user.email = :email")

    List<Reservation> getReservationsByUserAndDate(String email, LocalDateTime date);
}
