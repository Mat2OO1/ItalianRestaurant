package com.example.italianrestaurant.table.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> getAllByUserEmail(String email);

    @Query("FROM Reservation r WHERE (r.reservationDateStart >= :timeFrom AND r.reservationDateStart <= :timeTo)")
    List<Reservation> getReservedTables(LocalDateTime timeFrom, LocalDateTime timeTo);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.table.id = :tableId " +
            "AND DATE(r.reservationDateStart) = DATE(:date)")
    List<Reservation> getReservationForTable(int tableId, LocalDate date);
}
