package com.example.italianrestaurant.passwordreset;

import com.example.italianrestaurant.email.Email;
import com.example.italianrestaurant.email.EmailService;
import com.example.italianrestaurant.exceptions.InvalidTokenException;
import com.example.italianrestaurant.passwordreset.passwordtoken.PasswordToken;
import com.example.italianrestaurant.passwordreset.passwordtoken.PasswordTokenService;
import com.example.italianrestaurant.user.User;
import com.example.italianrestaurant.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserService userService;
    private final PasswordTokenService tokenService;
    private final EmailService emailService;

    @Value("${server-url}")
    private String serverUrl;

    public PasswordToken sendResetPasswordRequest(String email) throws MessagingException {

        User user = userService.getUserByEmail(email);

        String token = tokenService.generateToken();
        PasswordToken passwordToken = tokenService.saveToken(token, user);

        String resetUrl = serverUrl + "/reset-password?token=" + token;
        Email emailObject = emailService.buildPasswordResetEmail(email, resetUrl);
        emailService.sendHtmlMessage(emailObject);
        return passwordToken;
    }


    public void resetPassword(PasswordResetRequest request) throws InvalidTokenException, EntityNotFoundException {
        PasswordToken token = tokenService.getToken(request.getToken());
        User user = token.getUser();
        if (!tokenService.isValidToken(token)) {
            throw new InvalidTokenException();
        }

        userService.updatePassword(user, request.getPassword());
        tokenService.deleteToken(token);
    }
}
