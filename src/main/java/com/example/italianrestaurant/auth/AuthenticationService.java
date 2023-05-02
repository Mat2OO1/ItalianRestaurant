package com.example.italianrestaurant.auth;

import com.example.italianrestaurant.config.security.JwtService;
import com.example.italianrestaurant.user.Role;
import com.example.italianrestaurant.user.User;
import com.example.italianrestaurant.user.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EntityExistsException("User already exists");
        }

        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var expiration = jwtService.getTokenExpiration(jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .expiration(expiration)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(EntityNotFoundException::new);
        var jwtToken = jwtService.generateToken(user);
        var expiration = jwtService.getTokenExpiration(jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .expiration(expiration)
                .role(user.getRole())
                .build();
    }

}