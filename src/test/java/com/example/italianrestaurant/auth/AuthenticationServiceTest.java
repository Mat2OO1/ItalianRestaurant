package com.example.italianrestaurant.auth;

import com.example.italianrestaurant.Utils;
import com.example.italianrestaurant.config.security.JwtService;
import com.example.italianrestaurant.user.Role;
import com.example.italianrestaurant.user.User;
import com.example.italianrestaurant.user.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthenticationService authenticationService;


    @Test
    void shouldAuthenticate() {
        //given
        val authenticationRequest = AuthenticationRequest.builder()
                .email("email@email.com")
                .password("password")
                .build();

        val user = Utils.getUser();
        user.setId(1);
        val token = "token";
        val date = new Date();
        val authenticationResponse = AuthenticationResponse.builder()
                .token(token)
                .expiration(date)
                .build();

        given(authenticationManager.authenticate(any())).willReturn(null);
        given(userRepository.findByEmail(authenticationRequest.getEmail())).willReturn(Optional.of(user));
        given(jwtService.generateToken(user)).willReturn(token);
        given(jwtService.getTokenExpiration(token)).willReturn(date);

        //when
        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);

        //then
        assertThat(response).isEqualTo(authenticationResponse);
    }

    @Test
    void shouldNotAuthenticate() {
        //given
        val authenticationRequest = AuthenticationRequest.builder()
                .email("email@email.com")
                .password("password")
                .build();

        val user = Utils.getUser();
        user.setId(1);
        val token = "token";
        val date = new Date();
        val authenticationResponse = AuthenticationResponse.builder()
                .token(token)
                .expiration(date)
                .build();

        given(authenticationManager.authenticate(any())).willThrow(BadCredentialsException.class);


        //when
        assertThatThrownBy(() -> authenticationService.authenticate(authenticationRequest)).
                isInstanceOf(BadCredentialsException.class);

        //then
        verify(jwtService, times(0)).generateToken(any());
    }

    @Test
    void shouldRegister() {
        //given
        val registerRequest = RegisterRequest.builder()
                .email("email")
                .password("password")
                .firstname("firstName")
                .lastname("lastName")
                .build();
        val token = "token";
        val date = new Date();
        val authenticationResponse = AuthenticationResponse.builder()
                .token(token)
                .expiration(date)
                .build();
        val user = Utils.getUser();
        user.setRole(Role.USER);

        given(userRepository.existsByEmail(registerRequest.getEmail())).willReturn(false);
        given(passwordEncoder.encode(registerRequest.getPassword())).willReturn("password");
        given(userRepository.save(any(User.class))).willReturn(user);
        given(jwtService.generateToken(any(User.class))).willReturn(token);
        given(jwtService.getTokenExpiration(token)).willReturn(date);

        //when
        AuthenticationResponse response = authenticationService.register(registerRequest);

        //then
        assertThat(response).isEqualTo(authenticationResponse);
    }

    @Test
    void shouldNotRegister() {
        //given
        val registerRequest = RegisterRequest.builder()
                .email("email")
                .password("password")
                .firstname("firstName")
                .lastname("lastName")
                .build();

        given(userRepository.existsByEmail(registerRequest.getEmail())).willReturn(true);

        //when
        assertThatThrownBy(() -> authenticationService.register(registerRequest)).
                isInstanceOf(EntityExistsException.class);

        //then
        verify(jwtService, times(0)).generateToken(any());
    }


}
