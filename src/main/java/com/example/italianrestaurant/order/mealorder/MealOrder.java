package com.example.italianrestaurant.order.mealorder;

import com.example.italianrestaurant.meal.Meal;
import com.example.italianrestaurant.order.OrderStatus;
import jakarta.persistence.*;
import lombok.ToString;

@Entity
public class MealOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @ToString.Exclude
    private Meal meal;

    @Column
    private int quantity;

    @Column
    private double price;

    @Enumerated
    private OrderStatus orderStatus;
}
