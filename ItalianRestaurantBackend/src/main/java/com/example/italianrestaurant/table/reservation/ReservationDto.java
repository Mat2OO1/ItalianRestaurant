package com.example.italianrestaurant.table.reservation;

import com.example.italianrestaurant.table.Table;
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
    private Table table;
    private LocalDateTime reservationDateStart;
}
