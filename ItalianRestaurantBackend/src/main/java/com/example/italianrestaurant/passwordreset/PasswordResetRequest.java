package com.example.italianrestaurant.passwordreset;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordResetRequest {
    @NotBlank(message = "Token is mandatory")
    private String token;
    @NotBlank(message = "Password is mandatory")
    private String password;
}
