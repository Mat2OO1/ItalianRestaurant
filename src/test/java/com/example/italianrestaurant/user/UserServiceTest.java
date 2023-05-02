package com.example.italianrestaurant.user;

import com.example.italianrestaurant.Utils;
import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldGetUserByEmail() {
        //given
        val user = Utils.getUser();
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        //when
        val userByEmail = userService.getUserByEmail(user.getEmail());
        //then
        assertThat(userByEmail).isEqualTo(user);
    }

    @Test
    void shouldNotGetUserByEmail() {
        //given
        val user = Utils.getUser();
        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> userService.getUserByEmail(user.getEmail()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldUpdatePassword() {
        //given
        val user = Utils.getUser();
        val password = "password";
        given(passwordEncoder.encode(password)).willReturn("encodedPassword");
        given(userRepository.save(user)).willReturn(user);
        given(userRepository.existsByEmail(user.getEmail())).willReturn(true);
        //when
        userService.updatePassword(user, password);
        //then
        assertThat(user.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    void shouldNotUpdatePassword() {
        //given
        val user = Utils.getUser();
        val password = "password";
        given(passwordEncoder.encode(password)).willReturn("encodedPassword");
        given(userRepository.existsByEmail(user.getEmail())).willReturn(false);
        //when
        assertThatThrownBy(() -> userService.updatePassword(user, password))
                .isInstanceOf(EntityNotFoundException.class);
        //then
        verify(userRepository, times(0)).save(any());
    }
}
