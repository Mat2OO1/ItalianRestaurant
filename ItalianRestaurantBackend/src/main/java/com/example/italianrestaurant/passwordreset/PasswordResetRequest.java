package com.example.italianrestaurant.passwordreset;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = ".*[^a-zA-Z0-9].*", message = "Password must contain at least one special character")
    @NotBlank(message = "Password is mandatory")
    private String password;
}
