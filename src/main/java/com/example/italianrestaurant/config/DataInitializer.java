package com.example.italianrestaurant.config;

import com.example.italianrestaurant.meal.Meal;
import com.example.italianrestaurant.meal.MealRepository;
import com.example.italianrestaurant.meal.mealcategory.MealCategory;
import com.example.italianrestaurant.meal.mealcategory.MealCategoryRepository;
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
        return List.of(new Meal("Carbonara", "https://cdn.galleries.smcloud.net/t/photos/gf-Jyiw-FVWU-vX4E_spaghetti-carbonara-jak-zrobic-szybko-te-odmiane-pasty.jpg", mealCategoryRepository.findById(2L).get(),
                "grana padano, pasta, basil", 35.50),
                new Meal("Aglio Olio", "https://assets.tmecosys.com/image/upload/t_web767x639/img/recipe/ras/Assets/F5853F13-70A5-4A54-B38A-8F229C3050F5/Derivates/7C5F0049-A335-40D1-8EF5-D2702653584E.jpg", mealCategoryRepository.findById(2L).get(),
                        "olive oil, garlic, pasta", 15.99),
                new Meal("Margherita", "https://cdn.galleries.smcloud.net/t/galleries/gf-th4D-DoeK-NWgH_pizza-margherita-skad-pochodzi-nazwa-jpg-1920x1080-nocrop.jpg", mealCategoryRepository.findById(1L).get(),
                        "tomato sauce, cheese", 50),
                new Meal("Clams", "https://cdn.apartmenttherapy.info/image/upload/f_jpg,q_auto:eco,c_fill,g_auto,w_1500,ar_4:3/k%2FPhoto%2FRecipes%2F2019-07-recipe-20-minute-garlic-butter-steamed-clams%2F20-Minute-Garlic-Butter-Steamed-Clams_021", mealCategoryRepository.findById(3L).get(),
                        "clams", 30.25));
    }

    private List<MealCategory> getMealCategoriesData() {
        return List.of(new MealCategory("pizza", "https://www.foodandwine.com/thmb/97PY4E6Wk95IYv1_8pDZvBEi0Uw=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/cream-tomato-rigatoni-FT-RECIPE1020-139fb3fa52574e8bb06f98e7fa3e4f1e.jpg"),
                new MealCategory("pasta", "https://cdn.galleries.smcloud.net/t/galleries/gf-cgdk-p5yy-aE4f_pizza-pepperoni-z-jalapeno-to-jadl-joe-biden-z-zolnierzami-w-rzeszowie-1920x1080-nocrop.jpg"),
                new MealCategory("seafood", "https://www.foodandwine.com/thmb/tjkyiJutr0DdYGtGFy_hpcN0bSQ=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/seafood-blog618-f86e8baba8834537bbffa1d55e71c999.jpg"));
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
}
