package com.example.italianrestaurant.order.mealorder;

import com.example.italianrestaurant.meal.Meal;
import com.example.italianrestaurant.order.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "meal_orders")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private Meal meal;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private double price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    @ToString.Exclude
    @JsonIgnore
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
