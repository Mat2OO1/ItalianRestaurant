package com.example.italianrestaurant.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeRequest {

    @Email
    private String email;
    @NotBlank(message = "Current password is mandatory")
    private String currentPassword;
    @NotBlank(message = "New password is mandatory")
    private String newPassword;
}
