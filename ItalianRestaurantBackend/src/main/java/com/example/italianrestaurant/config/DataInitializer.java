package com.example.italianrestaurant.config;

import com.example.italianrestaurant.meal.Meal;
import com.example.italianrestaurant.meal.MealRepository;
import com.example.italianrestaurant.meal.mealcategory.MealCategory;
import com.example.italianrestaurant.meal.mealcategory.MealCategoryRepository;
import com.example.italianrestaurant.table.Table;
import com.example.italianrestaurant.table.TableRepository;
import com.example.italianrestaurant.table.TableStatus;
import com.example.italianrestaurant.user.AuthProvider;
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
    private final TableRepository tableRepository;

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

        if (tableRepository.count() == 0) {
            tableRepository.saveAll(getTableData());
        }
    }

    private List<Meal> getMealData() {
        return List.of(
                Meal.builder()
                        .name("Carbonara")
                        .imgPath("assets/images/meals/pasta/pasta_1.jpg")
                        .mealCategory(mealCategoryRepository.findById(2L).get())
                        .description("grana padano, pasta, basil")
                        .price(35.50)
                        .build(),

                Meal.builder()
                        .name("Aglio Olio")
                        .imgPath("assets/images/meals/pasta/pasta_2.jpg")
                        .mealCategory(mealCategoryRepository.findById(2L).get())
                        .description("olive oil, garlic, pasta")
                        .price(15.99)
                        .build(),

                Meal.builder()
                        .name("Margherita")
                        .imgPath("assets/images/meals/pizza/pizza_1.jpg")
                        .mealCategory(mealCategoryRepository.findById(1L).get())
                        .description("tomato sauce, cheese")
                        .price(50)
                        .build(),

                Meal.builder()
                        .name("Clams")
                        .imgPath("assets/images/meals/seafood/seafood_1.jpg")
                        .mealCategory(mealCategoryRepository.findById(3L).get())
                        .description("clams")
                        .price(30.25)
                        .build(),

                Meal.builder()
                        .name("Hawaiian")
                        .imgPath("assets/images/meals/pizza/pizza_2.jpg")
                        .mealCategory(mealCategoryRepository.findById(1L).get())
                        .description("pizza with ham and pineapple")
                        .price(20.20)
                        .build(),

                Meal.builder()
                        .name("Cacio e Pepe")
                        .imgPath("assets/images/meals/pasta/pasta_3.jpg")
                        .mealCategory(mealCategoryRepository.findById(2L).get())
                        .description("pasta with pecorino romano and pepper")
                        .price(15.99)
                        .build(),

                Meal.builder()
                        .name("Tagliatelle al Ragu")
                        .imgPath("assets/images/meals/pasta/pasta_4.jpg")
                        .mealCategory(mealCategoryRepository.findById(2L).get())
                        .description("tagliatelle pasta with classical italian meat sauce")
                        .price(25.99)
                        .build(),

                Meal.builder()
                        .name("Fritto Misto")
                        .imgPath("assets/images/meals/seafood/seafood_2.jpg")
                        .mealCategory(mealCategoryRepository.findById(3L).get())
                        .description("italian seafood platter")
                        .price(15.49)
                        .build()
        );
    }

    private List<MealCategory> getMealCategoriesData() {
        return List.of(
                MealCategory.builder()
                        .name("pizza")
                        .imgPath("assets/images/categories/pizza.jpg")
                        .build(),

                MealCategory.builder()
                        .name("pasta")
                        .imgPath("assets/images/categories/pasta.jpg")
                        .build(),

                MealCategory.builder()
                        .name("seafood")
                        .imgPath("assets/images/categories/seafood.jpg")
                        .build()
        );
    }


    private List<User> getUserData() {
        return List.of(
                User.builder()
                        .email("pawel.kluska256@gmail.com")
                        .password(passwordEncoder.encode("password"))
                        .firstName("Pawel")
                        .lastName("Kluska")
                        .role(Role.USER)
                        .provider(AuthProvider.local)
                        .emailVerified(false)
                        .build(),
                User.builder()
                        .email("mateusz.krajewski@gmail.com")
                        .password(passwordEncoder.encode("password"))
                        .firstName("Mateusz")
                        .lastName("Krajewski")
                        .provider(AuthProvider.local)
                        .role(Role.USER)
                        .emailVerified(false)
                        .build(),
                User.builder()
                        .email("admin@example.com")
                        .password(passwordEncoder.encode("password"))
                        .firstName("Admin")
                        .lastName("Admin")
                        .provider(AuthProvider.local)
                        .role(Role.ADMIN)
                        .emailVerified(false)
                        .build());
    }

    private List<Table> getTableData() {
        return List.of(
                Table.builder()
                        .number(1)
                        .seats(4)
                        .status(TableStatus.FREE)
                        .build(),
                Table.builder()
                        .number(2)
                        .seats(4)
                        .status(TableStatus.FREE)
                        .build(),
                Table.builder()
                        .number(3)
                        .seats(5)
                        .status(TableStatus.FREE)
                        .build(),
                Table.builder()
                        .number(4)
                        .seats(3)
                        .status(TableStatus.FREE)
                        .build(),
                Table.builder()
                        .number(5)
                        .seats(2)
                        .status(TableStatus.FREE)
                        .build(),
                Table.builder()
                        .number(6)
                        .seats(2)
                        .status(TableStatus.FREE)
                        .build());
    }
}
