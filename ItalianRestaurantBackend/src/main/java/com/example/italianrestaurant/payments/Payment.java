package com.example.italianrestaurant.payments;

import com.example.italianrestaurant.order.Order;
import com.example.italianrestaurant.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sessionId;

    @Column(nullable = false)
    private boolean isPaid;

    private Long amount;

    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "payment")
    @JsonBackReference
    @ToString.Exclude
    private Order order;
}
