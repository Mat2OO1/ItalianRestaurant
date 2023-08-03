package com.example.italianrestaurant.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT t FROM Order t WHERE t.user.email = :email")
    List<Order> findAllByUserEmail(String email);

    @Query("SELECT t FROM Order t WHERE DATE(t.orderDate) = :date")
    List<Order> findAllFromToday(LocalDate date);
}
