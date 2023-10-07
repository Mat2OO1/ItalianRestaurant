package com.example.italianrestaurant.table;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TableDto {
    private long id;
    @NotNull(message = "Table number is mandatory")
    private long number;
    @NotNull(message = "Table seats number is mandatory")
    private int seats;
}