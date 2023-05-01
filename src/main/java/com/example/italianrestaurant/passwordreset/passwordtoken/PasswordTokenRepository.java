package com.example.italianrestaurant.passwordreset.passwordtoken;

import com.example.italianrestaurant.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Long> {
    Optional<PasswordToken> findTokenByTokenAndUser(String token, User user);
    void deleteByToken(String token);
    Optional<PasswordToken> findTokenByToken(String token);
}
