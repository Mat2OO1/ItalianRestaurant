package com.example.italianrestaurant.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT t FROM Order t WHERE t.user.email = :user_email AND t.payment.isPaid = :payment_paid")
    List<Order> findAllByUserEmailAndPaymentPaid(String user_email, boolean payment_paid);

    @Query("SELECT t FROM Order t WHERE DATE(t.orderDate) = :date AND t.payment.isPaid = :payment_paid")
    List<Order> findAllFromTodayAndPaymentPaid(LocalDate date, boolean payment_paid);

}
