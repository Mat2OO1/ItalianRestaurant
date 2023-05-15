package com.example.italianrestaurant.delivery;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {
    @NotBlank(message = "Address is mandatory")
    private String address;
    @NotBlank(message = "City is mandatory")
    private String city;
    @NotBlank(message = "Postal code is mandatory")
    private String postalCode;
    @Min(value = 0, message = "Floor must be greater than 0")
    private int floor;
    private String info;
    @JsonFormat(pattern = "YYYY-MM-ddTHH:mm:ss", locale = "en-GB")
    private LocalDateTime deliveryDate;
    private DeliveryOptions deliveryOptions;
}
