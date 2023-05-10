package com.example.italianrestaurant.passwordreset.passwordtoken;

import com.example.italianrestaurant.Utils;
import com.example.italianrestaurant.exceptions.TokenNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PasswordTokenServiceTest {

    @Mock
    private PasswordTokenRepository tokenRepository;

    @InjectMocks
    private PasswordTokenService passwordTokenService;

    @Test
    void shouldGenerateToken() {
        //given
        //when
        String token = passwordTokenService.generateToken();
        //then
        assertThat(token).isNotNull();
    }

    @Test
    void shouldSaveToken() {
        //given
        val passwordToken = Utils.getPasswordToken();
        val user = Utils.getUser();
        given(tokenRepository.save(passwordToken)).willReturn(passwordToken);
        //when
        passwordTokenService.saveToken(passwordToken.getToken(), user);
        //then
        verify(tokenRepository, times(1)).save(passwordToken);
    }

    @Test
    void shouldGetTokenByToken() throws TokenNotFoundException {
        //given
        val passwordToken = Utils.getPasswordToken();
        given(tokenRepository.findTokenByToken(passwordToken.getToken())).willReturn(Optional.of(passwordToken));
        //when
        val returnedToken = passwordTokenService.getToken(passwordToken.getToken());
        //then
        assertThat(returnedToken).isEqualTo(passwordToken);
    }

    @Test
    void shouldNotGetTokenByToken() {
        //given
        val passwordToken = Utils.getPasswordToken();
        given(tokenRepository.findTokenByToken(passwordToken.getToken())).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> passwordTokenService.getToken(passwordToken.getToken()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void shouldDeleteToken() {
        //given
        val passwordToken = Utils.getPasswordToken();
        given(tokenRepository.findTokenByToken(passwordToken.getToken())).willReturn(Optional.of(passwordToken));
        //when
        passwordTokenService.deleteToken(passwordToken);
        //then
        verify(tokenRepository, times(1)).delete(passwordToken);
    }

    @Test
    void shouldNotDeleteToken() {
        //given
        val passwordToken = Utils.getPasswordToken();
        given(tokenRepository.findTokenByToken(passwordToken.getToken())).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> passwordTokenService.deleteToken(passwordToken))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
