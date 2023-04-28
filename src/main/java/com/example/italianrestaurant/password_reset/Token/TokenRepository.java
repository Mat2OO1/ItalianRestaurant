package com.example.italianrestaurant.password_reset.Token;

import com.example.italianrestaurant.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findTokenByTokenAndUser(String token, User user);
    void deleteByToken(String token);
    Optional<Token> findTokenByToken(String token);
}
