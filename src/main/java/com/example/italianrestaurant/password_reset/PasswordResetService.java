package com.example.italianrestaurant.password_reset;

import com.example.italianrestaurant.email.EmailService;
import com.example.italianrestaurant.exceptions.InvalidTokenException;
import com.example.italianrestaurant.password_reset.Token.Token;
import com.example.italianrestaurant.exceptions.TokenNotFoundException;
import com.example.italianrestaurant.password_reset.Token.TokenService;
import com.example.italianrestaurant.user.User;
import com.example.italianrestaurant.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserService userService;
    private final TokenService tokenService;
    private final EmailService emailService;

    @Value("${server-url}")
    private String serverUrl;

    public void resetPasswordRequest(String email) {

        User user = userService.getUserByEmail(email);

        String token = tokenService.generateToken();
        tokenService.saveToken(token, user);

        String resetUrl = serverUrl + "/reset-password?token=" + token;
        emailService.sendMessage(email, "Click here to reset your password: " + resetUrl);
    }


    public void resetPassword(PasswordResetRequest request) throws TokenNotFoundException, InvalidTokenException {
        Token token = tokenService.getToken(request.getToken());
        User user = token.getUser();
        if (!tokenService.isValidToken(token)) {
            throw new InvalidTokenException();
        }

        userService.updatePassword(user, request.getPassword());
        tokenService.deleteToken(token);

    }
}
