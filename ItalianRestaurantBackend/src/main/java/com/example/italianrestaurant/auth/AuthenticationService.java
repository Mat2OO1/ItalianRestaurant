package com.example.italianrestaurant.auth;

import com.example.italianrestaurant.security.JwtService;
import com.example.italianrestaurant.security.UserPrincipal;
import com.example.italianrestaurant.user.*;
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
                .phoneNumber(request.getPhoneNumber())
                .newsletter(request.isNewsletter())
                .provider(AuthProvider.local)
                .emailVerified(false)
                .build();
        userRepository.save(user);
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        var jwtToken = jwtService.generateToken(userPrincipal);
        var expiration = jwtService.getTokenExpiration(jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .expiration(expiration)
                .role(Role.USER)
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(EntityNotFoundException::new);

        UserPrincipal userPrincipal = UserPrincipal.create(user);
        var jwtToken = jwtService.generateToken(userPrincipal);
        var expiration = jwtService.getTokenExpiration(jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .expiration(expiration)
                .role(user.getRole())
                .build();
    }

    public AuthenticationResponse getUser(String token) {
        var email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        var expiration = jwtService.getTokenExpiration(token);
        return AuthenticationResponse.builder()
                .token(token)
                .expiration(expiration)
                .role(user.getRole())
                .build();
    }
}