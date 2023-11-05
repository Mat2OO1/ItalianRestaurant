package com.example.italianrestaurant.table.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {
    private Long tableId;
    private LocalDateTime reservationDateStart;
    private LocalDateTime reservationDateEnd;
}
