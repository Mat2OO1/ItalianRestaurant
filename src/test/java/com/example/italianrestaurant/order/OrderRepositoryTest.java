package com.example.italianrestaurant.order;

import com.example.italianrestaurant.AbstractTestcontainers;
import com.example.italianrestaurant.Utils;
import com.example.italianrestaurant.delivery.DeliveryRepository;
import com.example.italianrestaurant.user.UserRepository;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        deliveryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        var delivery = Utils.getDelivery();
        var delivery2 = Utils.getDelivery();
        delivery2.setAddress("address2");
        delivery = deliveryRepository.save(delivery);
        delivery2 = deliveryRepository.save(delivery2);

        var user = Utils.getUser();
        var user2 = Utils.getUser();
        user2.setEmail("user2@email.com");
        userRepository.save(user);
        userRepository.save(user2);

        val order = Utils.getOrder();
        order.setUser(user);
        order.setDelivery(delivery);
        val order2 = Utils.getOrder();
        order2.setUser(user2);
        order2.setDelivery(delivery2);
        order2.setOrderDate(LocalDateTime.of(2023, 1, 2, 1, 0));
        orderRepository.save(order);
        orderRepository.save(order2);
    }

    @Test
    void shouldFindOrdersByUserEmail() {
        // given
        val user = Utils.getUser();
        // when
        val orders = orderRepository.findAllByUserEmail(user.getEmail());
        // then
        assertThat(orders).isNotEmpty();
        assertThat(orders).hasSize(1);
        assertThat(orders.stream().map(Order::getUser).allMatch(u -> u.getEmail().equals(user.getEmail()))).isTrue();
    }

    @Test
    void shouldNotFindOrdersByUserEmail() {
        // given
        val user = Utils.getUser();
        user.setEmail("wrongEmail");
        // when
        val orders = orderRepository.findAllByUserEmail(user.getEmail());
        // then
        assertThat(orders).isEmpty();
    }

    @Test
    void shouldFindAllOrdersFromToday() {
        // given
        // when
        val orders = orderRepository.findAllFromToday(LocalDate.of(2023, 1, 1));
        // then
        assertThat(orders).isNotEmpty();
        assertThat(orders).hasSize(1);
    }

    @Test
    void shouldNotFindAllOrdersFromToday() {
        // given
        // when
        val orders = orderRepository.findAllFromToday(LocalDate.of(2023, 1, 3));
        // then
        assertThat(orders).isEmpty();
    }
}
