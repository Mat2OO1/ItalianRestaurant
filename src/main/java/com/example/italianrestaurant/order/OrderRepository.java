package com.example.italianrestaurant.order;

import com.example.italianrestaurant.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hibernate.FetchMode.SELECT;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUser(User user);

    @Query("SELECT t FROM Order t WHERE DATE(t.orderDate) = :date")
    List<Order> findAllFromToday(LocalDate date);
}
