package com.example.italianrestaurant.config;

import com.example.italianrestaurant.meal.Meal;
import com.example.italianrestaurant.meal.MealRepository;
import com.example.italianrestaurant.meal.mealcategory.MealCategory;
import com.example.italianrestaurant.meal.mealcategory.MealCategoryRepository;
import com.example.italianrestaurant.user.Role;
import com.example.italianrestaurant.user.User;
import com.example.italianrestaurant.user.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MealCategoryRepository mealCategoryRepository;
    private final MealRepository mealRepository;

    @PostConstruct
    @Transactional
    public void addData() {
        if (userRepository.count() == 0) {
            userRepository.saveAll(getUserData());
        }

        if (mealCategoryRepository.count() == 0) {
            mealCategoryRepository.saveAll(getMealCategoriesData());
        }

        if (mealRepository.count() == 0) {
            mealRepository.saveAll(getMealData());
        }
    }

    private List<Meal> getMealData() {
        return List.of(new Meal("Carbonara", "images/carbonara.jpg", mealCategoryRepository.findById(1L).get(),
                "grana padano, pasta, basil", 35.50),
                new Meal("Aglio Olio", "images/aglio-olio.jpg", mealCategoryRepository.findById(2L).get(),
                        "olive oil, garlic, pasta", 15.99),
                new Meal("Margherita", "images/margherita.jpg", mealCategoryRepository.findById(1L).get(),
                        "tomato sauce, cheese", 50),
                new Meal("Clams", "images/clams.jpg", mealCategoryRepository.findById(4L).get(),
                        "clams", 30.25));
    }

    private List<MealCategory> getMealCategoriesData() {
        return List.of(new MealCategory("pizza"),
                new MealCategory("pasta"),
                new MealCategory("meat dish"),
                new MealCategory("seafood"));
    }


    private List<User> getUserData() {
        return List.of(new User("pawel.kluska256@gmail.com", passwordEncoder.encode("password"),
                        "Pawel", "Kluska", Role.USER),
                new User("mateusz.krajewski@gmail.com", passwordEncoder.encode("password"),
                        "Mateusz", "Krajewski", Role.USER),
                new User("admin@example.com", passwordEncoder.encode("password"),"Admin","Admin", Role.ADMIN)
                );
    }
}
