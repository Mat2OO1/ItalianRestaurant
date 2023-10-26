package com.example.italianrestaurant.payments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaidResponse {
    private String sessionId;
    private String url;
}
