package com.example.italianrestaurant.delivery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {
    private String address;
    private String city;
    private String postalCode;
    private String floor;
    private String info;
    private LocalDateTime deliveryDate;
    private DeliveryOptions deliveryOptions;
}
