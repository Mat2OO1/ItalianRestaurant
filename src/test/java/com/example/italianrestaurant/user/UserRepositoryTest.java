package com.example.italianrestaurant.user;

import com.example.italianrestaurant.Utils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        var user = Utils.getUser();
        user = userRepository.save(user);
    }


    @Test
    void shouldFindByEmail() {
        // given
        var email = "email";
        // when
        var user = userRepository.findByEmail(email);

        // then
        assertThat(user).isPresent();
        assertThat(user.get().getEmail()).isEqualTo(email);
    }

    @Test
    void shouldNotFindByEmail() {
        //given
        var email = "wrongEmail";
        //when
        var user = userRepository.findByEmail(email);
        //then
        assertThat(user).isEmpty();
    }

    @Test
    void existsByEmailTest() {
        //given
        var email = "email";
        //when
        var exists = userRepository.existsByEmail(email);
        //then
        assertThat(exists).isTrue();
    }

    @Test
    void notExistsByEmailTest() {
        //given
        var email = "wrongEmail";
        //when
        var exists = userRepository.existsByEmail(email);
        //then
        assertThat(exists).isFalse();
    }
}
