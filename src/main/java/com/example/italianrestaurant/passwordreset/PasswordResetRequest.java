package com.example.italianrestaurant.passwordreset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordResetRequest {
    private String token;
    private String password;
}
