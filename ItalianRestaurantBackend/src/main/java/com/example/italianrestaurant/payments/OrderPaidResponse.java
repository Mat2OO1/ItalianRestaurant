package com.example.italianrestaurant.payments;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderPaidResponse {
    private String sessionId;
    private String url;
}
