package com.example.italianrestaurant.passwordreset;

import com.example.italianrestaurant.exceptions.InvalidTokenException;
import jakarta.mail.MessagingException;
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
            return ResponseEntity.ok(passwordResetService.sendResetPasswordRequest(email));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (MailException | MessagingException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request) {
        try {
            passwordResetService.resetPassword(request);
            return ResponseEntity.ok().body("Password retested");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Token not found");
        } catch (InvalidTokenException e) {
            return ResponseEntity.badRequest().body("Invalid token");
        }
    }
}
