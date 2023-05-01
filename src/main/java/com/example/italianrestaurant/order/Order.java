package com.example.italianrestaurant.order;

import com.example.italianrestaurant.order.mealorder.MealOrder;
import com.example.italianrestaurant.user.User;
import com.example.italianrestaurant.delivery.Delivery;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private User user;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn
    @ToString.Exclude
    private Delivery delivery;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<MealOrder> mealOrders;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(user, order.user) && Objects.equals(delivery, order.delivery) && Objects.equals(mealOrders, order.mealOrders) && orderStatus == order.orderStatus && Objects.equals(orderDate, order.orderDate);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
