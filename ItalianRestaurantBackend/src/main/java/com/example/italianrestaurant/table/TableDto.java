package com.example.italianrestaurant.table;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
    @Min(value = 1, message = "Table number is mandatory")
    private long number;
    @Min(value = 1, message = "Table seats number is mandatory")
    private int seats;
}