package com.example.italianrestaurant.meal;

import com.example.italianrestaurant.meal.mealcategory.MealCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Table(name = "meals")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String name_pl;
    private String image;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private MealCategory mealCategory;
    private String description;
    private String description_pl;
    @Column(nullable = false)
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
