package com.example.italianrestaurant.meal.mealcategory;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class MealCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MealCategory mealCategory = (MealCategory) o;
        return getId() != null && Objects.equals(getId(), mealCategory.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
