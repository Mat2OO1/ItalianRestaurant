package com.example.italianrestaurant.payments;

import com.example.italianrestaurant.Utils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @AfterEach
    void tearDown() {
        paymentRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        var payment = Utils.getPayment();
        paymentRepository.save(payment);
    }

    @Test
    void shouldFindPaymentBySessionId() {
        var payment = paymentRepository.findBySessionId("sessionId");
        assertThat(payment).isNotNull();
    }
}
