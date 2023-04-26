package com.example.italianrestaurant.order;

import com.example.italianrestaurant.delivery.Delivery;
import com.example.italianrestaurant.delivery.DeliveryDto;
import com.example.italianrestaurant.delivery.DeliveryService;
import com.example.italianrestaurant.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final DeliveryService deliveryService;

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findAllByUser(user);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order makeOrder(User user, DeliveryDto deliveryDto) {
        Delivery dbDelivery = deliveryService.addDelivery(deliveryDto);
        Order order = Order.builder()
                .delivery(dbDelivery)
//                .orderDate(LocalDateTime.now())
                .user(user)
                .build();

        return orderRepository.save(order);
    }
}
