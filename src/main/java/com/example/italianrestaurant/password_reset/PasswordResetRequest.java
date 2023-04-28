package com.example.italianrestaurant.password_reset;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String token;
    private String password;
}
