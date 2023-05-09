package com.example.italianrestaurant.passwordreset;

import com.example.italianrestaurant.exceptions.InvalidTokenException;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "There is no user with email:" + email, e);
        } catch (MailException | MessagingException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY, "Mail service is not available", e);
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        try {
            passwordResetService.resetPassword(request);
            return ResponseEntity.ok().body("Password retested");
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Token not found", e);
        }
    }
}
