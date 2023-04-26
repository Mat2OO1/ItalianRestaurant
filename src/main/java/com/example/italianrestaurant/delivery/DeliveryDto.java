package com.example.italianrestaurant.delivery;

import lombok.Data;

@Data
public class DeliveryDto {
    private String address;
    private String city;
    private String postalCode;
    private String floor;
    private String info;
    private DeliveryOptions deliveryOptions;
}
