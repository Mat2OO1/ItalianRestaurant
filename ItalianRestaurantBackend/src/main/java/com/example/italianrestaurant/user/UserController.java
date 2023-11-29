package com.example.italianrestaurant.user;

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
    public ResponseEntity<User> updateUser(@Valid @RequestBody UserDto userDto) {
        try {
            return ResponseEntity.ok(userService.updateUser(userDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok().build();
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
}
