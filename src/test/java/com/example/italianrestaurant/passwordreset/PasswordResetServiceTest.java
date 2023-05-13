package com.example.italianrestaurant.passwordreset;

import com.example.italianrestaurant.Utils;
import com.example.italianrestaurant.email.EmailService;
import com.example.italianrestaurant.passwordreset.passwordtoken.PasswordToken;
import com.example.italianrestaurant.passwordreset.passwordtoken.PasswordTokenService;
import com.example.italianrestaurant.user.User;
import com.example.italianrestaurant.user.UserService;
import jakarta.mail.MessagingException;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordResetServiceTest {

    @Mock
    UserService userService;
    @Mock
    PasswordTokenService passwordTokenService;
    @Mock
    EmailService emailService;
    @InjectMocks
    PasswordResetService passwordResetService;


    @Test
    void shouldSendResetPasswordRequest() throws MessagingException {
        //given
        val email = "email";
        val token = "token";
        val emailObject = Utils.getEmail();

        given(userService.getUserByEmail(email)).willReturn(Utils.getUser());
        given(passwordTokenService.generateToken()).willReturn(token);
        given(passwordTokenService.saveToken(any(), any(User.class))).willReturn(Utils.getPasswordToken());
        given(emailService.buildPasswordResetEmail(eq(email), any())).willReturn(emailObject);
        doNothing().when(emailService).sendHtmlMessage(emailObject);

        //when
        PasswordToken passwordToken = passwordResetService.sendResetPasswordRequest(email);

        //then
        assertThat(passwordToken.getToken()).isNotNull();
        verify(emailService, times(1)).sendHtmlMessage(emailObject);

    }

    @Test
    void shouldNotSendResetRequestWhenUserNotFound() throws MessagingException {
        //given
        val email = "email";
        given(userService.getUserByEmail(email)).willThrow(new RuntimeException("User not found"));

        //when
        assertThatThrownBy(() -> passwordResetService.sendResetPasswordRequest(email))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");

        //then
        verify(emailService, never()).sendHtmlMessage(any());
    }

    @Test
    void shouldNotSendResetRequestWhenMailNotAvailable() throws MessagingException {
        //given
        val email = "email";
        val token = "token";
        val passwordToken = Utils.getPasswordToken();
        val user = Utils.getUser();
        val emailObject = Utils.getEmail();

        given(userService.getUserByEmail(email)).willReturn(user);
        given(passwordTokenService.generateToken()).willReturn(token);
        given(emailService.buildPasswordResetEmail(eq(email), any())).willReturn(emailObject);
        doThrow(new MessagingException()).when(emailService).sendHtmlMessage(emailObject);

        //when
        //then
        assertThatThrownBy(() -> passwordResetService.sendResetPasswordRequest(email))
                .isInstanceOf(MessagingException.class);

    }


    @Test
    void shouldResetPassword() {
        //given
        val token = Utils.getPasswordToken();
        val user = Utils.getUser();
        token.setUser(user);

        given(passwordTokenService.getToken(token.getToken())).willReturn(token);

        //when
        passwordResetService.resetPassword(new PasswordResetRequest(token.getToken(), "password"));

        //then
        verify(userService, times(1)).updatePassword(user, "password");
        verify(passwordTokenService, times(1)).deleteToken(token);
    }
}
