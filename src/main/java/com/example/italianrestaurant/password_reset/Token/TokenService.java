package com.example.italianrestaurant.password_reset.Token;

import com.example.italianrestaurant.exceptions.TokenNotFoundException;
import com.example.italianrestaurant.user.User;
import com.example.italianrestaurant.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private static final int EXPIRATION_TIME = 60;

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void saveToken(String token, User user) {
        long expiryTime = System.currentTimeMillis() + (EXPIRATION_TIME * 60 * 1000);
        Token tokenEntity = Token.builder()
                .expiryTime(expiryTime)
                .user(user)
                .token(token).build();
        tokenRepository.save(tokenEntity);
    }

    public Token getToken(String token) throws TokenNotFoundException {
        return tokenRepository.findTokenByToken(token).orElseThrow(TokenNotFoundException::new);
    }

    public boolean isValidToken(Token token) {
        long currentTime = System.currentTimeMillis();
        return token != null &&
                token.getExpiryTime() != null &&
                token.getExpiryTime() >= currentTime;
    }

    public void deleteToken(Token token) {
        tokenRepository.delete(token);
    }
}