package com.example.italianrestaurant.passwordreset.passwordtoken;

import com.example.italianrestaurant.exceptions.TokenNotFoundException;
import com.example.italianrestaurant.user.User;
import jakarta.persistence.EntityNotFoundException;
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

    public PasswordToken saveToken(String token, User user) {
        long expiryTime = System.currentTimeMillis() + (EXPIRATION_TIME * 60 * 1000);
        PasswordToken tokenEntity = PasswordToken.builder()
                .expiryTime(expiryTime)
                .user(user)
                .token(token).build();
        return tokenRepository.save(tokenEntity);
    }

    public PasswordToken getToken(String token) {
        return tokenRepository.findTokenByToken(token).orElseThrow(EntityNotFoundException::new);
    }

    public void deleteToken(PasswordToken token) {
        if (tokenRepository.findTokenByToken(token.getToken()).isEmpty())
            throw new EntityNotFoundException();
        tokenRepository.delete(token);
    }
}