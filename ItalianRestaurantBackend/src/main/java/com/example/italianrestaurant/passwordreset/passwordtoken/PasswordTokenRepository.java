package com.example.italianrestaurant.passwordreset.passwordtoken;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Long> {
    void deleteByToken(String token);

    Optional<PasswordToken> findTokenByToken(String token);
}
