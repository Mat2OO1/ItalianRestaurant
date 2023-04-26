package com.example.italianrestaurant.meal;

import com.example.italianrestaurant.meal.mealcategory.MealCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "meals")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String imgPath;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    @ToString.Exclude
    private MealCategory mealCategory;
    private String description;
    private double price;

    public Meal(String name, String imgPath, MealCategory mealCategory, String description, double price) {
        this.name = name;
        this.imgPath = imgPath;
        this.mealCategory = mealCategory;
        this.description = description;
        this.price = price;
    }

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
