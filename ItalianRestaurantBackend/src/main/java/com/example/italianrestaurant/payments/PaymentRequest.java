package com.example.italianrestaurant.payments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PaymentRequest {
    private String userEmail;
    private String productName;
    private double price;
    private long quantity;
}
