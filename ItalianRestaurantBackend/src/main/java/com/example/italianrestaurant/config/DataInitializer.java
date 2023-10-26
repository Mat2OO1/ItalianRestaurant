package com.example.italianrestaurant.config;

import com.example.italianrestaurant.aws.AwsService;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MealCategoryRepository mealCategoryRepository;
    private final MealRepository mealRepository;
    private final TableRepository tableRepository;
    private final AwsService awsService;

    @PostConstruct
    @Transactional
    public void addData() throws IOException {
        if (userRepository.count() == 0) {
            userRepository.saveAll(getUserData());
        }

        if (mealCategoryRepository.count() == 0 || mealRepository.count() == 0) {
            // TODO: when project finished uncomment this line
            // awsService.deleteAllImages();
            mealRepository.deleteAll();
            mealCategoryRepository.deleteAll();

            mealCategoryRepository.saveAll(getMealCategoriesData());
            mealRepository.saveAll(getMealData());
        }

        if (tableRepository.count() == 0) {
            tableRepository.saveAll(getTableData());
        }
    }

    private List<Meal> getMealData() throws IOException {

        String pasta1Image = uploadImage("meals/pasta/pasta_1.jpg");
        String pasta2Image = uploadImage("meals/pasta/pasta_2.jpg");
        String pasta3Image = uploadImage("meals/pasta/pasta_3.jpg");
        String pasta4Image = uploadImage("meals/pasta/pasta_4.jpg");

        String pizza1Image = uploadImage("meals/pizza/pizza_1.jpg");
        String pizza2Image = uploadImage("meals/pizza/pizza_2.jpg");

        String seafood1Image = uploadImage("meals/seafood/seafood_1.jpg");
        String seafood2Image = uploadImage("meals/seafood/seafood_2.jpg");

        return List.of(

                Meal.builder()
                        .name("Carbonara")
                        .image(pasta1Image)
                        .mealCategory(mealCategoryRepository.findById(2L).get())
                        .description("grana padano, pasta, basil")
                        .price(35.50)
                        .build(),

                Meal.builder()
                        .name("Aglio Olio")
                        .image(pasta2Image)
                        .mealCategory(mealCategoryRepository.findById(2L).get())
                        .description("olive oil, garlic, pasta")
                        .price(15.99)
                        .build(),

                Meal.builder()
                        .name("Margherita")
                        .image(pizza1Image)
                        .mealCategory(mealCategoryRepository.findById(1L).get())
                        .description("tomato sauce, cheese")
                        .price(50)
                        .build(),

                Meal.builder()
                        .name("Clams")
                        .image(seafood1Image)
                        .mealCategory(mealCategoryRepository.findById(3L).get())
                        .description("clams")
                        .price(30.25)
                        .build(),

                Meal.builder()
                        .name("Hawaiian")
                        .image(pizza2Image)
                        .mealCategory(mealCategoryRepository.findById(1L).get())
                        .description("pizza with ham and pineapple")
                        .price(20.20)
                        .build(),

                Meal.builder()
                        .name("Cacio e Pepe")
                        .image(pasta3Image)
                        .mealCategory(mealCategoryRepository.findById(2L).get())
                        .description("pasta with pecorino romano and pepper")
                        .price(15.99)
                        .build(),

                Meal.builder()
                        .name("Tagliatelle al Ragu")
                        .image(pasta4Image)
                        .mealCategory(mealCategoryRepository.findById(2L).get())
                        .description("tagliatelle pasta with classical italian meat sauce")
                        .price(25.99)
                        .build(),

                Meal.builder()
                        .name("Fritto Misto")
                        .image(seafood2Image)
                        .mealCategory(mealCategoryRepository.findById(3L).get())
                        .description("italian seafood platter")
                        .price(15.49)
                        .build()
        );
    }

    private List<MealCategory> getMealCategoriesData() throws IOException {

        String pizzaImage = uploadImage("categories/pizza.jpg");
        String pastaImage = uploadImage("categories/pasta.jpg");
        String seafoodImage = uploadImage("categories/seafood.jpg");

        return List.of(
                MealCategory.builder()
                        .name("pizza")
                        .image(pizzaImage)
                        .build(),

                MealCategory.builder()
                        .name("pasta")
                        .image(pastaImage)
                        .build(),

                MealCategory.builder()
                        .name("seafood")
                        .image(seafoodImage)
                        .build()
        );
    }

    private String uploadImage(String path) throws IOException {
        InputStream is1 = getClass().getClassLoader().getResourceAsStream("images/" + path);
        if (is1 != null) {
            byte[] image1 = is1.readAllBytes();
            is1.close();
            return awsService.getObjectUrl(awsService.uploadFile(image1, "image/jpeg"));
        }
        return "";
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
