package com.example.italianrestaurant.table.reservation;

import com.example.italianrestaurant.security.UserPrincipal;
import com.example.italianrestaurant.table.Table;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/add")
    public ResponseEntity<?> addReservation(@RequestBody ReservationDto reservationDto, @AuthenticationPrincipal UserPrincipal userPrincipal){
        try{
            Reservation reservation = this.reservationService.addReservation(userPrincipal,reservationDto);
            return ResponseEntity.ok(reservation);
        }
        catch (UserReservationConflictException | TableAlreadyReservedException e ){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancelReservation(@RequestParam Long reservationId){
        try{
            this.reservationService.deleteReservation(reservationId);
        } catch(EntityNotFoundException ex){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public ResponseEntity<List<Reservation>> getReservations(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.ok(this.reservationService.getReservations(userPrincipal));
    }

    @GetMapping("/reserved/all")
    public ResponseEntity<List<Table>> getReservedTables(){
        return ResponseEntity.ok(this.reservationService.getReservedTables());
    }

    @GetMapping("/reserved/{date}")
    public ResponseEntity<List<Reservation>> getReservedTables(@PathVariable LocalDate date){
        return ResponseEntity.ok(this.reservationService.getReservationsForDate(date));
    }

    @GetMapping("/reserved")
    public ResponseEntity<List<LocalDateTime>> getReservationsForTable(@RequestParam("tableId") Long tableId, @RequestParam("date") LocalDate date){
        return ResponseEntity.ok(this.reservationService.getReservationsForTable(tableId, date));
    }
}
