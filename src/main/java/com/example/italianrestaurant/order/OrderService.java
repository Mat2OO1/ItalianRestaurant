package com.example.italianrestaurant.order;

import com.example.italianrestaurant.delivery.Delivery;
import com.example.italianrestaurant.delivery.DeliveryDto;
import com.example.italianrestaurant.delivery.DeliveryService;
import com.example.italianrestaurant.exceptions.InvalidEntityException;
import com.example.italianrestaurant.order.mealorder.MealOrder;
import com.example.italianrestaurant.order.mealorder.MealOrderService;
import com.example.italianrestaurant.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final DeliveryService deliveryService;
    private final ModelMapper modelMapper;
    private final MealOrderService mealOrderService;

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findAllByUser(user);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order makeOrder(User user, OrderDto orderDto) throws InvalidEntityException {
        Delivery dbDelivery = deliveryService.addDelivery(orderDto.getDelivery());

        List<MealOrder> mealOrders = orderDto.getMealOrders().stream()
                .map(mealOrderDto -> modelMapper.map(mealOrderDto, MealOrder.class))
                .toList();

        Optional<Boolean> first = mealOrders.stream().map(mealOrderService::isMealOrderValid)
                .filter(v -> !v)
                .findFirst();
        if (first.isPresent()) throw new InvalidEntityException();

        Order order = Order.builder()
                .delivery(dbDelivery)
                .orderDate(LocalDateTime.now())
                .user(user)
                .mealOrders(mealOrders)
                .orderStatus(orderDto.getOrderStatus())
                .build();

        return orderRepository.save(order);
    }

    public Order changeStatus(ChangeOrderStatusDto orderDto) {
        Optional<Order> order = orderRepository.findById(orderDto.getOrderId());
        if (order.isEmpty())
            throw new EntityNotFoundException("Order with id " + orderDto.getOrderId() + " not found");

        order.get().setOrderStatus(orderDto.getOrderStatus());
        order.get().getDelivery().setDeliveryDate(orderDto.getDeliveryDate());
        return orderRepository.save(order.get());

    }
}
