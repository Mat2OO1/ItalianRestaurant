package com.example.italianrestaurant.passwordreset;

import com.example.italianrestaurant.email.EmailEntity;
import com.example.italianrestaurant.email.EmailService;
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

    @Value("${app.front-url}")
    private String frontUrl;

    public PasswordToken sendResetPasswordRequest(String email, String lang) throws MessagingException {

        User user = userService.getUserByEmail(email);

        String token = tokenService.generateToken();
        String resetUrl = frontUrl + "/password?token=" + token;
        EmailEntity emailObject = emailService.buildPasswordResetEmail(email, resetUrl, lang);
        emailService.sendHtmlMessage(emailObject);
        return tokenService.saveToken(token, user);
    }


    public void resetPassword(PasswordResetRequest request) throws EntityNotFoundException {
        PasswordToken token = tokenService.getToken(request.getToken());
        User user = token.getUser();

        userService.updatePassword(user, request.getPassword());
        tokenService.deleteToken(token);
    }
}
