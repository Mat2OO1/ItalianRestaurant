package com.example.italianrestaurant.order;

import com.example.italianrestaurant.Utils;
import com.example.italianrestaurant.delivery.Delivery;
import com.example.italianrestaurant.delivery.DeliveryRepository;
import com.example.italianrestaurant.user.User;
import com.example.italianrestaurant.user.UserRepository;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderRepositoryTest {

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
        orderRepository.save(order);
        orderRepository.save(order2);
    }

    @Test
    void shouldFindOrdersByUser() {
        // given
        val user = Utils.getUser();
        user.setId(1);
        // when
        val orders = orderRepository.findAllByUser(user);
        // then
        assertThat(orders).isNotEmpty();
        assertThat(orders).hasSize(1);
        assertThat(orders.stream().map(Order::getUser).allMatch(u -> u.getId() == 1)).isTrue();

    }

    @Test
    void shouldNotFindOrdersByUser() {
        // given
        val user = Utils.getUser();
        user.setId(Integer.MAX_VALUE);
        // when
        val orders = orderRepository.findAllByUser(user);
        // then
        assertThat(orders).isEmpty();
    }
}
