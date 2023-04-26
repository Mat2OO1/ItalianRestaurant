package com.example.italianrestaurant.meal.mealcategory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "meal_categories")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class MealCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public MealCategory(String name) {
        this.name = name;
    }

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
