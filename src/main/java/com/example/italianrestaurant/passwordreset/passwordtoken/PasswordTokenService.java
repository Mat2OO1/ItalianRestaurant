package com.example.italianrestaurant.passwordreset.passwordtoken;

import com.example.italianrestaurant.exceptions.TokenNotFoundException;
import com.example.italianrestaurant.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordTokenService {

    private final PasswordTokenRepository tokenRepository;
    private static final int EXPIRATION_TIME = 60;

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void saveToken(String token, User user) {
        long expiryTime = System.currentTimeMillis() + (EXPIRATION_TIME * 60 * 1000);
        PasswordToken tokenEntity = PasswordToken.builder()
                .expiryTime(expiryTime)
                .user(user)
                .token(token).build();
        tokenRepository.save(tokenEntity);
    }

    public PasswordToken getToken(String token) throws TokenNotFoundException {
        return tokenRepository.findTokenByToken(token).orElseThrow(TokenNotFoundException::new);
    }

    public boolean isValidToken(PasswordToken token) {
        long currentTime = System.currentTimeMillis();
        return token != null &&
                token.getExpiryTime() != null &&
                token.getExpiryTime() >= currentTime;
    }

    public void deleteToken(PasswordToken token) {
        tokenRepository.delete(token);
    }
}