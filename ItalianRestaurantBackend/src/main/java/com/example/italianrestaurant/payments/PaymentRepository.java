package com.example.italianrestaurant.payments;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findBySessionId(String sessionId);
}
