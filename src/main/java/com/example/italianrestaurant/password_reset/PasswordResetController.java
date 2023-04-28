package com.example.italianrestaurant.password_reset;

import com.example.italianrestaurant.exceptions.InvalidTokenException;
import com.example.italianrestaurant.exceptions.TokenNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @GetMapping("/request")
    public ResponseEntity<?> resetPasswordRequest(@RequestParam String email) {
        try {
            passwordResetService.resetPasswordRequest(email);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (MailException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request) {
        try {
            passwordResetService.resetPassword(request);
            return ResponseEntity.ok().build();
        } catch (TokenNotFoundException e) {
            return ResponseEntity.badRequest().body("Token not found");
        } catch (InvalidTokenException e) {
            return ResponseEntity.badRequest().body("Invalid token");
        }
    }
}
