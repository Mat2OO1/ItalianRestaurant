package com.example.italianrestaurant.order.mealorder;

import com.example.italianrestaurant.meal.Meal;
import com.example.italianrestaurant.order.Order;
import com.example.italianrestaurant.order.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "meal_orders")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class MealOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @ToString.Exclude
    private Meal meal;

    private int quantity;

    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @ToString.Exclude
    private Order order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MealOrder mealOrder = (MealOrder) o;
        return getId() != null && Objects.equals(getId(), mealOrder.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
