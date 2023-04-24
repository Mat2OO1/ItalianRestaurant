package com.example.italianrestaurant.meal;

import com.example.italianrestaurant.meal.mealcategory.MealCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String imgPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @ToString.Exclude
    private MealCategory mealCategory;

    @Column
    private String description;

    @Column
    private double price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Meal meal = (Meal) o;
        return getId() != null && Objects.equals(getId(), meal.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
