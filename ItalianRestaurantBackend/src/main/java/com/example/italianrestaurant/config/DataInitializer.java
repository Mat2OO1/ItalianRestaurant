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

        String pasta1Image = uploadImage("meals/pasta/pasta_1.webp");
        String pasta2Image = uploadImage("meals/pasta/pasta_2.webp");
        String pasta3Image = uploadImage("meals/pasta/pasta_3.webp");
        String pasta4Image = uploadImage("meals/pasta/pasta_4.webp");
        String pasta5Image = uploadImage("meals/pasta/pasta_5.webp");
        String pasta6Image = uploadImage("meals/pasta/pasta_6.webp");
        String pasta7Image = uploadImage("meals/pasta/pasta_7.webp");


        String pizza1Image = uploadImage("meals/pizza/pizza_1.webp");
        String pizza2Image = uploadImage("meals/pizza/pizza_2.webp");
        String pizza3Image = uploadImage("meals/pizza/pizza_3.webp");
        String pizza4Image = uploadImage("meals/pizza/pizza_4.webp");
        String pizza5Image = uploadImage("meals/pizza/pizza_5.webp");
        String pizza6Image = uploadImage("meals/pizza/pizza_6.webp");
        String pizza7Image = uploadImage("meals/pizza/pizza_7.webp");
        String pizza8Image = uploadImage("meals/pizza/pizza_8.webp");
        String pizza9Image = uploadImage("meals/pizza/pizza_9.webp");
        String pizza10Image = uploadImage("meals/pizza/pizza_10.webp");
        String pizza11Image = uploadImage("meals/pizza/pizza_11.webp");
        String pizza12Image = uploadImage("meals/pizza/pizza_12.webp");
        String pizza13Image = uploadImage("meals/pizza/pizza_13.webp");
        String pizza14Image = uploadImage("meals/pizza/pizza_14.webp");


        String desserts1Image = uploadImage("meals/desserts/DOLCE-CIOCCOLATO.webp");
        String desserts2Image = uploadImage("meals/desserts/PANNA-COTTA.webp");
        String desserts3Image = uploadImage("meals/desserts/CREME-BRULEE.webp");
        String desserts4Image = uploadImage("meals/desserts/MOUSSE-AL-PISTACCHIO.webp");
        String desserts5Image = uploadImage("meals/desserts/SOUFFLÉ-AL-CIOCCOLATO.webp");
        String desserts6Image = uploadImage("meals/desserts/TORTA-AL-MANGO.webp");
        String desserts7Image = uploadImage("meals/desserts/TIRAMISU.webp");


        String drinks1Image = uploadImage("meals/drinks/TEA.webp");
        String drinks2Image = uploadImage("meals/drinks/ESPRESSO.webp");
        String drinks3Image = uploadImage("meals/drinks/ESPRESSO DOPPIO.webp");
        String drinks4Image = uploadImage("meals/drinks/ESPRESSO MACCHIATO.webp");
        String drinks5Image = uploadImage("meals/drinks/AMERICANO.webp");
        String drinks6Image = uploadImage("meals/drinks/CAPPUCCINO.webp");
        String drinks7Image = uploadImage("meals/drinks/LATTE.webp");
        String drinks8Image = uploadImage("meals/drinks/LATTE MACCHIATO.webp");
        String drinks9Image = uploadImage("meals/drinks/COCA-COLA, COLA ZERO, FANTA, SPRITE, KINLEY.webp");
        String drinks10Image = uploadImage("meals/drinks/ITALIAN MINERAL WATER SAN BERNARDO 0,75L.webp");
        String drinks11Image = uploadImage("meals/drinks/FRESH PRESSED JUICE 0,35L.webp");
        String drinks12Image = uploadImage("meals/drinks/HOME-MADE WINTER ICED TEA 0.9L.webp");
        String drinks13Image = uploadImage("meals/drinks/HOME-MADE WINTER LEMONADE 0,9L.webp");


        String sides1Image = uploadImage("meals/sides/sides_1.webp");
        String sides2Image = uploadImage("meals/sides/sides_2.webp");
        String sides3Image = uploadImage("meals/sides/sides_3.webp");


        String kids1Image = uploadImage("meals/kids/kids_1.webp");
        String kids2Image = uploadImage("meals/kids/kids_2.webp");
        String kids3Image = uploadImage("meals/kids/kids_3.webp");
        return List.of(

                Meal.builder()
                        .name("MARGHERITA")
                        .name_pl("MARGHERITA")
                        .image(pizza1Image)
                        .mealCategory(mealCategoryRepository.findById(1L).get())
                        .description("Home-made garlic-infused olive oil, Grana Padano cheese 32cm")
                        .description_pl("Oliwa czosnkowa, ser Grana Padano 32cm")
                        .price(37.99)
                        .build(),

                Meal.builder()
                        .name("PEPERONI")
                        .name_pl("PEPERONI")
                        .image(pizza2Image)
                        .mealCategory(mealCategoryRepository.findById(1L).get())
                        .description("Pepperoni sausage, home-made garlic-infused olive oil, Grana Padano cheese 32cm")
                        .description_pl("Kiełbasa pepperoni, oliwa czosnkowa, ser Grana Padano 32cm")
                        .price(42.99)
                        .build(),

                Meal.builder()
                        .name("COTTO")
                        .name_pl("COTTO")
                        .image(pizza3Image)
                        .mealCategory(mealCategoryRepository.findById(1L).get())
                        .description("Cotto ham, home-made garlic-infused olive oil, Grana Padano cheese 32cm")
                        .description_pl("Szynka Cotto, oliwa czosnkowa, ser Grana Padano 32cm")
                        .price(42.99)
                        .build(),

                Meal.builder()
                        .name("HAWAII")
                        .name_pl("HAWAJSKA")
                        .image(pizza4Image)
                        .mealCategory(mealCategoryRepository.findById(1L).get())
                        .description("Cotto ham, pineapple, home-made garlic-infused olive oil, Grana Padano cheese 32cm")
                        .description_pl("Szynka Cotto, ananas, oliwa czosnkowa, ser Grana Padano 32cm")
                        .price(42.99)
                        .build(),

                Meal.builder()
                        .name("MADDALENA")
                        .name_pl("MADDALENA")
                        .image(pizza5Image)
                        .mealCategory(mealCategoryRepository.findById(1L).get())
                        .description("PIZZA WITHOUT CHEESE: tomato sauce, stewed champignons, red onion, zucchini, artichokes, sundried tomatoes, black olives, jalapeno peppers, home-made garlic-infused olive oil 32cm")
                        .description_pl("PIZZA BEZ SERA: sos pomidorowy, duszone pieczarki, czerwona cebula, cukinia, karczochy, suszone pomidory, czarne oliwki, siekane papryczki jalapeno, oliwa czosnkowa 32cm")
                        .price(39.99)
                        .build(),

                Meal.builder()
                        .name("BUFALA")
                        .name_pl("BUFALA")
                        .image(pizza6Image)
                        .mealCategory(mealCategoryRepository.findById(1L).get())
                        .description("Italian mozzarella cheese made from the milk of the water buffalo,cherry tomatoes, fresh basil, home-made garlic-infused olive oil 32cm")
                        .description_pl("Włoska mozzarella z bawolego mleka,pomidorki cherry, świeża bazylia, oliwa czosnkowa 32cm")
                        .price(44.99)
                        .build(),

                Meal.builder()
                        .name("COTTO E FUNGHI")
                        .name_pl("COTTO E FUNGHI")
                        .image(pizza7Image)
                        .mealCategory(mealCategoryRepository.findById(1L).get())
                        .description("Cotto ham, fresh champignons, home-made garlic-infused olive oil, Grana Padano cheese 32cm")
                        .description_pl("Szynka Cotto, świeże pieczarki, oliwa czosnkowa, ser Grana Padano 32cm")
                        .price(42.99)
                        .build(),

                Meal.builder()
                        .name("CAPRICCIOSA")
                        .name_pl("CAPRICCIOSA")
                        .image(pizza8Image)
                        .mealCategory(mealCategoryRepository.findById(1L).get())
                        .description("Cotto Ham, marinated red peppers, stewed champignons, pesto, home-made garlic-infused olive oil, Grana Padano cheese 32cm")
                        .description_pl("Szynka Cotto, marynowana czerwona papryka, duszone pieczarki, pesto, oliwa czosnkowa, ser Grana Padano 32cm")
                        .price(44.99)
                        .build(),

                Meal.builder()
                        .name("DECO")
                        .name_pl("DECO")
                        .image(pizza9Image)
                        .mealCategory(mealCategoryRepository.findById(1L).get())
                        .description("Chicken breast marinated in garlic and chilli, red onion, marinated red peppers, home-made garlic-infused olive oil, Grana Padano cheese 32cm")
                        .description_pl("Kurczak marynowany w czosnku i chilli, czerwona cebula, marynowana czerwona papryka, oliwa czosnkowa, ser Grana Padano 32cm")
                        .price(41.99)
                        .build(),

                Meal.builder()
                        .name("AMERICANA")
                        .name_pl("AMERICANA")
                        .image(pizza10Image)
                        .mealCategory(mealCategoryRepository.findById(1L).get())
                        .description("Pepperoni sausage, fresh arugula, home-made garlic-infused olive oil, Grana Padano cheese 32cm")
                        .description_pl("Kiełbasa pepperoni, świeża rukola, oliwa czosnkowa, ser Grana Padano 32cm")
                        .price(43.99)
                        .build(),

                Meal.builder()
                        .name("ANCHOIS")
                        .name_pl("ANCHOIS")
                        .image(pizza11Image)
                        .mealCategory(mealCategoryRepository.findById(1L).get())
                        .description("Anchovies, capers, red onion, home-made garlic-infused olive oil, Grana Padano cheese 32cm")
                        .description_pl("Anchois, kapary, czerwona cebula, oliwa czosnkowa, ser Grana Padano 32cm")
                        .price(52.99)
                        .build(),

                Meal.builder()
                        .name("QUATTRO FORMAGGI")
                        .name_pl("CZTERY SERY")
                        .image(pizza12Image)
                        .mealCategory(mealCategoryRepository.findById(1L).get())
                        .description("Mozzarella cheese, Gorgonzola cheese, French cheese, home-made garlic-infused olive oil, Grana Padano cheese 32cm")
                        .description_pl("Ser mozzarella, ser Gorgonzola, ser francuski, oliwa czosnkowa, ser Grana Padano 32cm")
                        .price(50.99)
                        .build(),

                Meal.builder()
                        .name("NAPOLETANA")
                        .name_pl("NEAPOLITAŃSKA")
                        .image(pizza13Image)
                        .mealCategory(mealCategoryRepository.findById(1L).get())
                        .description("Bocconcini mozzarella, black olives, anchovies, capers, home-made garlic-infused olive oil, Grana Padano cheese 32cm")
                        .description_pl("Kulki sera Bocconcini, czarne oliwki, anchois, kapary, oliwa czosnkowa, ser Grana Padan 32cm")
                        .price(44.99)
                        .build(),

                Meal.builder()
                        .name("VEGETARIANA")
                        .name_pl("WEGETARIAŃSKA")
                        .image(pizza14Image)
                        .mealCategory(mealCategoryRepository.findById(1L).get())
                        .description("Goat cheese, marinated red peppers, marinated artichokes, spinach, stewed champignons, pesto, home-made garlic-infused olive oil, Grana Padano cheese 32cm")
                        .description_pl("Ser kozi, marynowana czerwona papryka, marynowane karczochy, szpinak, duszone pieczarki, pesto, oliwa czosnkowa, ser Grana Padano 32cm")
                        .price(43.99)
                        .build(),



                Meal.builder()
                        .name("SPAGHETTI AGLIO OLIO")
                        .name_pl("SPAGHETTI AGLIO OLIO")
                        .image(pasta1Image)
                        .mealCategory(mealCategoryRepository.findById(2L).get())
                        .description("Troccoli pasta spiced with extra-virgin olive oil, garlic and chilli peppers, topped with parsley and Grana Padano cheese")
                        .description_pl("Makaron Troccoli z oliwą z pierwszego tłoczenia, czosnkiem, papryczką chilli, posypany pietruszką i serem Grana Padano")
                        .price(29)
                        .build(),

                Meal.builder()
                        .name("TAGLIATELLE RAGU BOLOGNESE")
                        .name_pl("TAGLIATELLE RAGU BOLOGNESE")
                        .image(pasta2Image)
                        .mealCategory(mealCategoryRepository.findById(2L).get())
                        .description("Troccoli egg pasta with tomato-based vegetable ragout with minced beef and red wine, topped with Grana Padano cheese")
                        .description_pl("Makaron jajeczny tagliatelle z sosem pomidorowo-warzywnym, mielonym mięsem wołowym, czerwonym winem, posypany serem Grana Padano")
                        .price(44)
                        .build(),

                Meal.builder()
                        .name("TAGLIATELLE PESTO SICILIANO")
                        .name_pl("TAGLIATELLE PESTO SICILIANO")
                        .image(pasta3Image)
                        .mealCategory(mealCategoryRepository.findById(2L).get())
                        .description("Tagliatelle egg pasta tossed with pesto, Ricotta cheese and cherry tomatoes, topped with Grana Padano cheese")
                        .description_pl("Makaron jajeczny tagliatelle z zielonym pesto, serem Ricotta i pomidorkami cherry, posypany serem Grana Padano")
                        .price(45)
                        .build(),

                Meal.builder()
                        .name("GNOCCHI PARMA E PESTO")
                        .name_pl("GNOCCHI PARMA E PESTO")
                        .image(pasta4Image)
                        .mealCategory(mealCategoryRepository.findById(2L).get())
                        .description("Italian potato dumplings served with pesto, Prosciutto Crudo ham and cherry tomatoes, topped with Grana Padano cheese")
                        .description_pl("Kopytka ziemniaczane gnocchi z sosem z zielonego pesto, szynką Prosciutto Crudo i pomidorkami cherry, posypane serem Grana Padano")
                        .price(42)
                        .build(),

                Meal.builder()
                        .name("SPAGHETTI CARBONARA")
                        .name_pl("SPAGHETTI CARBONARA")
                        .image(pasta5Image)
                        .mealCategory(mealCategoryRepository.findById(2L).get())
                        .description("Troccoli egg pasta with traditional Roman sauce, made with egg yolk, fried Italian bacon and ground black pepper, topped with Grana Padano cheese")
                        .description_pl("Makaron jajeczny troccoli z tradycyjnym rzymskim sosem na bazie żółtka, prażonego włoskiego boczku i czarnego pieprzu, posypany serem Grana Padano")
                        .price(36)
                        .build(),

                Meal.builder()
                        .name("RISOTTO POLLO")
                        .name_pl("RISOTTO POLLO")
                        .image(pasta6Image)
                        .mealCategory(mealCategoryRepository.findById(2L).get())
                        .description("Traditional Italian risotto with chicken breast marinated in garlic and chilli and green peas, topped with parsley and Grana Padano cheese")
                        .description_pl("Tradycyjne włoskie risotto z kurczakiem marynowanym w czosnku i chilli oraz groszkiem, posypane pietruszką i serem Grana Padano")
                        .price(32)
                        .build(),

                Meal.builder()
                        .name("RISOTTO PARMA E PISTACCHIO")
                        .name_pl("RISOTTO PARMA E PISTACCHIO")
                        .image(pasta7Image)
                        .mealCategory(mealCategoryRepository.findById(2L).get())
                        .description("Traditional Italian risotto with pistachio pesto, Prosciutto Crudo ham, pear, Gorgonzola cheese and crushed pistachios, topped with Grana Padano cheese")
                        .description_pl("Tradycyjne włoskie risotto z pesto pistacjowym, szynką Prosciutto Crudo, marynowaną gruszką, serem Gorgonzola i kruszonymi pistacjami, posypane serem Grana Pa")
                        .price(48)
                        .build(),



                Meal.builder()
                        .name("DOLCE CIOCCOLATO")
                        .name_pl("DOLCE CIOCCOLATO")
                        .image(desserts1Image)
                        .mealCategory(mealCategoryRepository.findById(3L).get())
                        .description("Rich chocolate cake served with a vanilla ice-cream and pistachio sauce")
                        .description_pl("Ciastko z gęstej masy czekoladowej, podane z kulką lodów waniliowych i sosem pistacjowym")
                        .price(29.99)
                        .build(),

                Meal.builder()
                        .name("PANNA COTTA")
                        .name_pl("PANNA COTTA")
                        .image(desserts2Image)
                        .mealCategory(mealCategoryRepository.findById(3L).get())
                        .description("Traditional Italian pudding-style dessert made of cooled cream, served with home-made sauce: chocolate / raspberry / mango / salty caramel")
                        .description_pl("Tradycyjny włoski deser waniliowy na zimno podany z domowej produkcji sosem: czekoladowy / mus malinowy / mus mango / słony karmel")
                        .price(25.99)
                        .build(),

                Meal.builder()
                        .name("CREME BRULEE")
                        .name_pl("CREME BRULEE")
                        .image(desserts3Image)
                        .mealCategory(mealCategoryRepository.findById(3L).get())
                        .description("Vanilla custard base topped with a layer of hard caramel")
                        .description_pl("Krem waniliowy uwieńczony warstwą skarmelizowanego cukru")
                        .price(29.99)
                        .build(),

                Meal.builder()
                        .name("MOUSSE AL PISTACCHIO")
                        .name_pl("MOUSSE AL PISTACCHIO")
                        .image(desserts4Image)
                        .mealCategory(mealCategoryRepository.findById(3L).get())
                        .description("Pistachio mousse with pistachios and chocolate crumble")
                        .description_pl("Mus pistacjowy z pistacjami i kruszonką czekoladową")
                        .price(33.99)
                        .build(),

                Meal.builder()
                        .name("SOUFFLÉ AL CIOCCOLATO")
                        .name_pl("SOUFFLÉ AL CIOCCOLATO")
                        .image(desserts5Image)
                        .mealCategory(mealCategoryRepository.findById(3L).get())
                        .description("Chocolate soufflé served warm, filled with liquid dark chocolate, served with a vanilla ice-cream and with Crema Pasticcera")
                        .description_pl("Suflet na ciepło wypełniony płynną czekoladą, podany z kulką lodów waniliowych i kremem Pasticcera")
                        .price(33.99)
                        .build(),

                Meal.builder()
                        .name("TORTA AL MANGO")
                        .name_pl("TORTA AL MANGO")
                        .image(desserts6Image)
                        .mealCategory(mealCategoryRepository.findById(3L).get())
                        .description("Italian cheesecake on crunchy muesli crust, with pineapple-mango jelly, served with home-made mango sauce")
                        .description_pl("Włoski sernik na chrupiącym spodzie owsianym z galaretką z mango i ananasa, polany sosem z mango")
                        .price(28.99)
                        .build(),

                Meal.builder()
                        .name("TIRAMISU")
                        .name_pl("TIRAMISU")
                        .image(desserts7Image)
                        .mealCategory(mealCategoryRepository.findById(3L).get())
                        .description("Classic Italian dessert, flavoured with amaretto liquor")
                        .description_pl("Klasyczny włoski deser z dodatkiem amaretto")
                        .price(28.99)
                        .build(),



                Meal.builder()
                        .name("TEA")
                        .name_pl("HERBATA")
                        .image(drinks1Image)
                        .mealCategory(mealCategoryRepository.findById(4L).get())
                        .description("Black, strong and energizing tea. Excellent mixture of tea leaves from Ceylon, Darjeeling and Assam")
                        .description_pl("Czarna, mocna i energetyzująca herbata. Doskonała mieszanka liści herbaty z Cejlonu, Darjeeling i Assam")
                        .price(6)
                        .build(),

                Meal.builder()
                        .name("ESPRESSO")
                        .name_pl("ESPRESSO")
                        .image(drinks2Image)
                        .mealCategory(mealCategoryRepository.findById(4L).get())
                        .description("Short strong black coffee")
                        .description_pl("Mała mocna czarna kawa o wyrazistym smaku i aromacie")
                        .price(10)
                        .build(),

                Meal.builder()
                        .name("ESPRESSO DOPPIO")
                        .name_pl("ESPRESSO DOPPIO")
                        .image(drinks3Image)
                        .mealCategory(mealCategoryRepository.findById(4L).get())
                        .description("Double espresso")
                        .description_pl("Podwójne espresso")
                        .price(14)
                        .build(),

                Meal.builder()
                        .name("ESPRESSO MACCHIATO")
                        .name_pl("ESPRESSO MACCHIATO")
                        .image(drinks4Image)
                        .mealCategory(mealCategoryRepository.findById(4L).get())
                        .description("Espresso/ double espresso with a small amount of milk")
                        .description_pl("Pojedyncze/podwójne espresso z dodatkiem mleka i piany mlecznej")
                        .price(14)
                        .build(),

                Meal.builder()
                        .name("AMERICANO")
                        .name_pl("AMERICANO")
                        .image(drinks5Image)
                        .mealCategory(mealCategoryRepository.findById(4L).get())
                        .description("Single espresso with addition of hot water")
                        .description_pl("Pojedyncze espresso przedłużone gorącą wodą")
                        .price(15)
                        .build(),

                Meal.builder()
                        .name("CAPPUCCINO")
                        .name_pl("CAPPUCCINO")
                        .image(drinks6Image)
                        .mealCategory(mealCategoryRepository.findById(4L).get())
                        .description("Espresso with both steamed and frothed milk")
                        .description_pl("Pojedyncze espresso z dużą ilością piany mlecznej")
                        .price(15)
                        .build(),

                Meal.builder()
                        .name("LATTE")
                        .name_pl("LATTE")
                        .image(drinks7Image)
                        .mealCategory(mealCategoryRepository.findById(4L).get())
                        .description("Layered coffee drink made with espresso/ double espresso with steamed milk")
                        .description_pl("Pojedyncze/podwójne espresso z dużą ilością mleka")
                        .price(15)
                        .build(),

                Meal.builder()
                        .name("LATTE MACCHIATO")
                        .name_pl("LATTE MACCHIATO")
                        .image(drinks8Image)
                        .mealCategory(mealCategoryRepository.findById(4L).get())
                        .description("Layered coffee drink made with espresso/ double espresso and foamed milk")
                        .description_pl("Pojedyncze/podwójne espresso z mlekiem i pianą mleczną")
                        .price(16)
                        .build(),

                Meal.builder()
                        .name("COCA-COLA, COLA ZERO, FANTA, SPRITE, KINLEY")
                        .name_pl("COCA-COLA, COLA ZERO, FANTA, SPRITE, KINLEY")
                        .image(drinks9Image)
                        .mealCategory(mealCategoryRepository.findById(4L).get())
                        .description("Drinking capacity - 0.25L")
                        .description_pl("Pojemność napoju - 0.25L")
                        .price(9)
                        .build(),

                Meal.builder()
                        .name("ITALIAN MINERAL WATER SAN BERNARDO 0,75L")
                        .name_pl("WŁOSKA WODA MINERALNA SAN BENARDO 0,75L")
                        .image(drinks10Image)
                        .mealCategory(mealCategoryRepository.findById(4L).get())
                        .description("Sparkling or still")
                        .description_pl("Gazowana lub niegazowana")
                        .price(15)
                        .build(),

                Meal.builder()
                        .name("FRESH PRESSED JUICE 0,35L")
                        .name_pl("SOK ŚWIEŻO WYCISKANY 0,35L")
                        .image(drinks11Image)
                        .mealCategory(mealCategoryRepository.findById(4L).get())
                        .description("Fresh pressed juice (orange, red grapefruit or mixed)")
                        .description_pl("Świeżo wyciskany sok z pomarańczy, grejpfrutów lub mieszany")
                        .price(18)
                        .build(),

                Meal.builder()
                        .name("HOME-MADE WINTER ICED TEA 0.9L")
                        .name_pl("DOMOWA ZIMOWA MROŻONA HERBATA 0.9L")
                        .image(drinks12Image)
                        .mealCategory(mealCategoryRepository.findById(4L).get())
                        .description("Home-made iced tea, made with lemon and honey")
                        .description_pl("Domowa mrożona herbata z dodatkiem cytryny i miodu")
                        .price(19)
                        .build(),

                Meal.builder()
                        .name("HOME-MADE WINTER LEMONADE 0,9L")
                        .name_pl("DOMOWA ZIMOWA LEMONIADA 0,9L")
                        .image(drinks13Image)
                        .mealCategory(mealCategoryRepository.findById(4L).get())
                        .description("Home-made lemonade, made with honey, fresh mint and orange slices")
                        .description_pl("Domowy napój cytrynowy z dodatkiem miodu, mięty i pomarańczy")
                        .price(19)
                        .build(),



                Meal.builder()
                        .name("OLIVES & FETA BREAD")
                        .name_pl("PIECZYWO OLIWKOWE")
                        .image(sides1Image)
                        .mealCategory(mealCategoryRepository.findById(5L).get())
                        .description("Pizza dough oven-baked with black olives and feta cheese, served with black olive pesto, home-made garlic-infused olive oil and Grana Padano cheese")
                        .description_pl("Pieczywo wypiekane z włoskiego ciasta drożdżowego z czarnymi oliwkami i serem feta, pesto z czarnych oliwek, oliwą czosnkową, posypane serem Grana Padano")
                        .price(8)
                        .build(),

                Meal.builder()
                        .name("BRUSCHETTA POMODORO")
                        .name_pl("BRUSCHETTA POMIDOROWA")
                        .image(sides2Image)
                        .mealCategory(mealCategoryRepository.findById(5L).get())
                        .description("Italian grilled wheat bread topped with chopped tomatoes, goat cheese, pesto and Balsamico sauce")
                        .description_pl("Grzanki z włoskiego pieczywa z drobno posiekanymi pomidorami, serem kozim, bazylią, pesto i sosem balsamicznym")
                        .price(12)
                        .build(),

                Meal.builder()
                        .name("SET OF 3 SAUCES")
                        .name_pl("ZESTAW 3 SOSÓW")
                        .image(sides3Image)
                        .mealCategory(mealCategoryRepository.findById(5L).get())
                        .description("Tomato, garlic, mayonnaise and mustard")
                        .description_pl("Pomidorowy, czosnkowy, majonezowo-musztardowy")
                        .price(6)
                        .build(),



                Meal.builder()
                        .name("BROTH BRODO")
                        .name_pl("ROSÓŁ BRODO")
                        .image(kids1Image)
                        .mealCategory(mealCategoryRepository.findById(6L).get())
                        .description("Beef broth with carrot and pasta")
                        .description_pl("Rosół wołowy z marchewką i makaronem")
                        .price(12)
                        .build(),

                Meal.builder()
                        .name("PICCOLO PASTA")
                        .name_pl("PICCOLO PASTA")
                        .image(kids2Image)
                        .mealCategory(mealCategoryRepository.findById(6L).get())
                        .description("Busiate pasta with butter")
                        .description_pl("Makaron busiate z masłem")
                        .price(17)
                        .build(),

                Meal.builder()
                        .name("PICCOLO RAGU BOLOGNESE")
                        .name_pl("PICCOLO RAGU BOLOGNESE")
                        .image(kids3Image)
                        .mealCategory(mealCategoryRepository.findById(6L).get())
                        .description("Troccoli egg pasta with tomato-based vegetable ragout with minced beef, topped with Grana Padano cheese")
                        .description_pl("Makaron jajeczny troccoli z sosem pomidorowo-warzywnym, mielonym mięsem wołowym, posypany serem Grana Padano\n")
                        .price(20)
                        .build()

        );
    }

    private List<MealCategory> getMealCategoriesData() throws IOException {

        return List.of(
                MealCategory.builder()
                        .name("pizza")
                        .name_pl("pizza")
                        .build(),

                MealCategory.builder()
                        .name("pasta")
                        .name_pl("makaron")
                        .build(),

                MealCategory.builder()
                        .name("desserts")
                        .name_pl("desery")
                        .build(),

                MealCategory.builder()
                        .name("drinks")
                        .name_pl("napoje")
                        .build(),

                MealCategory.builder()
                        .name("sides")
                        .name_pl("dodatki")
                        .build(),

                MealCategory.builder()
                        .name("for kids")
                        .name_pl("dla dzieci")
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
                        .email("bednarskii.sebastian@gmail.com")
                        .password(passwordEncoder.encode("password"))
                        .firstName("Sebastian")
                        .lastName("Bednarski")
                        .provider(AuthProvider.local)
                        .role(Role.USER)
                        .emailVerified(false)
                        .build(),
                User.builder()
                        .email("wojciech.dominiak@gmail.com")
                        .password(passwordEncoder.encode("password"))
                        .firstName("Wojciech")
                        .lastName("Dominiak")
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
//                        .status(TableStatus.FREE)
                        .build(),
                Table.builder()
                        .number(2)
                        .seats(4)
//                        .status(TableStatus.OCCUPIED)
                        .build(),
                Table.builder()
                        .number(3)
                        .seats(5)
//                        .status(TableStatus.FREE)
                        .build(),
                Table.builder()
                        .number(4)
                        .seats(3)
//                        .status(TableStatus.RESERVED)
                        .build(),
                Table.builder()
                        .number(5)
                        .seats(2)
//                        .status(TableStatus.OCCUPIED)
                        .build(),
                Table.builder()
                        .number(6)
                        .seats(2)
//                        .status(TableStatus.FREE)
                        .build());
    }
}
