package com.example.italianrestaurant.passwordreset.passwordtoken;

import com.example.italianrestaurant.Utils;
import com.example.italianrestaurant.user.UserRepository;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PasswordTokenRepositoryTest {

    @Autowired
    private PasswordTokenRepository passwordTokenRepository;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        passwordTokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        var user = Utils.getUser();
        user = userRepository.save(user);
        var passwordToken = Utils.getPasswordToken();
        passwordToken.setUser(user);
        passwordTokenRepository.save(passwordToken);
    }

    @Test
    void shouldFindTokenByToken() {
        //given
        val token = Utils.getPasswordToken();
        token.setId(1L);
        //when
        val passwordToken = passwordTokenRepository.findTokenByToken(token.getToken());

        //then
        assertThat(passwordToken).isPresent();
    }

    @Test
    void shouldNotFindTokenByToken() {
        //given
        val token = Utils.getPasswordToken();
        token.setId(1L);
        //when
        val passwordToken = passwordTokenRepository.findTokenByToken("wrongToken");

        //then
        assertThat(passwordToken).isEmpty();
    }

    @Test
    void shouldDeleteByToken() {
        //given
        val token = Utils.getPasswordToken();
        token.setId(1L);
        //when
        passwordTokenRepository.deleteByToken(token.getToken());
        val passwordToken = passwordTokenRepository.findTokenByToken(token.getToken());

        //then
        assertThat(passwordToken).isEmpty();
    }

    @Test
    void shouldNotDeleteByToken() {
        //given
        val token = Utils.getPasswordToken();
        token.setId(1L);
        //when
        passwordTokenRepository.deleteByToken("wrongToken");
        val passwordToken = passwordTokenRepository.findTokenByToken(token.getToken());

        //then
        assertThat(passwordToken).isPresent();
    }
}
