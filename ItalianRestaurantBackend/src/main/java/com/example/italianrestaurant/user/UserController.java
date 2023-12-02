package com.example.italianrestaurant.user;

import com.example.italianrestaurant.auth.AuthenticationResponse;
import com.example.italianrestaurant.security.UserPrincipal;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<AuthenticationResponse> updateUser(@AuthenticationPrincipal UserPrincipal user,
                                                             @RequestBody UserDto userDto) {
        try {
            return ResponseEntity.ok(userService.updateUser(userDto, user.getUsername()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<User> getUser(@AuthenticationPrincipal UserPrincipal user) {
        try {
            return ResponseEntity.ok(userService.getUserByEmail(user.getUsername()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/password")
    public ResponseEntity<?> changePassword(@AuthenticationPrincipal UserPrincipal user,
                                            @Valid @RequestBody PasswordChangeRequest request) {
        try {
            userService.changePassword(request, user.getUsername());
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException | WrongPasswordException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAccount(@AuthenticationPrincipal UserPrincipal user,
                                           @Valid @RequestBody DeleteUserDto request) {
        try {
            userService.deleteAccount(request.getPassword(), user.getUsername());
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
