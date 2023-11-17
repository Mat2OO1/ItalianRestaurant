package com.example.italianrestaurant.meal.mealcategory;

import com.example.italianrestaurant.meal.Meal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "meal_categories")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MealCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String name_pl;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "mealCategory", cascade = CascadeType.ALL)
    private List<Meal> meals;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MealCategory mealCategory = (MealCategory) o;
        return getId() != null && Objects.equals(getId(), mealCategory.getId());
    }
}
