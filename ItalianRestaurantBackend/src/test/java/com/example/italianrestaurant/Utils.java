package com.example.italianrestaurant;

import com.example.italianrestaurant.delivery.Delivery;
import com.example.italianrestaurant.delivery.DeliveryDto;
import com.example.italianrestaurant.delivery.DeliveryOptions;
import com.example.italianrestaurant.email.EmailEntity;
import com.example.italianrestaurant.meal.Meal;
import com.example.italianrestaurant.meal.MealDto;
import com.example.italianrestaurant.meal.mealcategory.MealCategory;
import com.example.italianrestaurant.order.ChangeOrderStatusDto;
import com.example.italianrestaurant.order.Order;
import com.example.italianrestaurant.order.OrderDto;
import com.example.italianrestaurant.order.OrderStatus;
import com.example.italianrestaurant.order.mealorder.MealOrder;
import com.example.italianrestaurant.order.mealorder.MealOrderDto;
import com.example.italianrestaurant.passwordreset.PasswordResetRequest;
import com.example.italianrestaurant.passwordreset.passwordtoken.PasswordToken;
import com.example.italianrestaurant.table.Table;
import com.example.italianrestaurant.table.TableDto;
import com.example.italianrestaurant.table.TableStatus;
import com.example.italianrestaurant.user.AuthProvider;
import com.example.italianrestaurant.user.Role;
import com.example.italianrestaurant.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.val;

import java.time.LocalDateTime;
import java.util.List;

public class Utils {

    public static MealDto getMealDto() {
        //given
        return MealDto.builder()
                .name("name")
                .category("name")
                .description("description")
                .imgPath("imgPath")
                .price(10.0)
                .build();
    }

    public static DeliveryDto getDeliveryDto() {
        return DeliveryDto.builder()
                .address("address")
                .city("city")
                .postalCode("postalCode")
                .floor(2)
                .info("info")
                .deliveryOptions(DeliveryOptions.KNOCK)
                .build();
    }

    public static Delivery getDelivery() {
        return Delivery.builder()
                .address("address")
                .city("city")
                .postalCode("postalCode")
                .floor("floor")
                .info("info")
                .deliveryDate(LocalDateTime.now())
                .deliveryOptions(DeliveryOptions.KNOCK)
                .build();
    }

    public static User getUser() {
        return User.builder()
                .password("password")
                .email("email")
                .firstName("firstName")
                .lastName("lastName")
                .role(Role.USER)
                .provider(AuthProvider.local)
                .build();
    }

    public static Order getOrder() {
        return Order.builder()
                .orderDate(LocalDateTime.of(2023, 1, 1, 1, 1))
                .orderStatus(OrderStatus.IN_PREPARATION)
                .build();
    }

    public static MealCategory getMealCategory() {
        return MealCategory.builder()
                .name("name")
                .build();
    }

    public static String objectToJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T jsonStringToObject(final String jsonString, Class<T> clazz) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Meal getMeal() {
        return Meal.builder()
                .name("name")
                .description("description")
                .price(10.0)
                .imgPath("imgPath")
                .build();
    }

    public static Meal getMealWithCategory() {
        return Meal.builder()
                .name("name")
                .mealCategory(getMealCategory())
                .description("description")
                .price(10.0)
                .imgPath("imgPath")
                .build();
    }


    public static MealOrderDto getMealOrderDto() {
        return MealOrderDto.builder()
                .quantity(3)
                .price(23)
                .build();
    }

    public static MealOrder getMealOrder() {
        return MealOrder.builder()
                .quantity(3)
                .price(23)
                .build();
    }

    public static OrderDto getOrderDto() {
        return OrderDto.builder()
                .orderStatus(OrderStatus.IN_PREPARATION)
                .delivery(getDeliveryDto())
                .mealOrders(List.of(getMealOrderDto()))
                .build();
    }

    public static ChangeOrderStatusDto getChangeOrderStatusDto() {
        return ChangeOrderStatusDto.builder()
                .orderStatus(OrderStatus.IN_PREPARATION)
                .orderId(1L)
                .deliveryDate(LocalDateTime.now())
                .build();
    }

    public static List<Order> buildOrders() {
        var delivery = Utils.getDelivery();
        delivery.setId(1L);
        var delivery2 = Utils.getDelivery();
        delivery2.setId(2L);
        delivery2.setAddress("address2");

        var user = Utils.getUser();
        user.setId(1L);
        var user2 = Utils.getUser();
        user2.setId(2L);
        user2.setEmail("user2@email.com");

        val order = Utils.getOrder();
        order.setId(1L);
        order.setUser(user);
        order.setDelivery(delivery);
        val order2 = Utils.getOrder();
        order2.setId(2L);
        order2.setUser(user2);
        order2.setDelivery(delivery2);
        return List.of(order, order2);
    }

    public static PasswordToken getPasswordToken() {
        return PasswordToken.builder()
                .token("token")
                .expiryTime(System.currentTimeMillis() + 10000)
                .user(Utils.getUser())
                .build();
    }

    public static EmailEntity getEmail() {
        return EmailEntity.builder()
                .to("email")
                .subject("Reset password")
                .text("content")
                .build();
    }

    public static PasswordResetRequest getPasswordResetRequest() {
        return PasswordResetRequest.builder()
                .password("password")
                .token("token")
                .build();
    }

    public static Table getTable() {
        return Table.builder()
                .number(1)
                .seats(2)
                .status(TableStatus.FREE)
                .build();
    }

    public static TableDto getTableDto() {
        return TableDto.builder()
                .number(1)
                .seats(2)
                .status(TableStatus.FREE)
                .build();
    }
}
