package com.example.italianrestaurant.table.reservation;

import com.example.italianrestaurant.security.UserPrincipal;
import com.example.italianrestaurant.table.Table;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reservations")

public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/add")
    public ResponseEntity<Reservation> addReservation(@RequestBody ReservationDto reservationDto, @AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.ok(this.reservationService.addReservation(userPrincipal,reservationDto));
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

    @GetMapping("")
    public ResponseEntity<List<Reservation>> getReservations(@AuthenticationPrincipal UserPrincipal userPrincipal){
        return ResponseEntity.ok(this.reservationService.getReservations(userPrincipal));
    }

    @GetMapping("reserved")
    public ResponseEntity<List<Table>> getReservedTables(){
        return ResponseEntity.ok(this.reservationService.getReservedTables());
    }
}
