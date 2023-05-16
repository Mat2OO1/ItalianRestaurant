package com.example.italianrestaurant.order;

import com.example.italianrestaurant.delivery.Delivery;
import com.example.italianrestaurant.delivery.DeliveryService;
import com.example.italianrestaurant.order.mealorder.MealOrder;
import com.example.italianrestaurant.order.mealorder.MealOrderService;
import com.example.italianrestaurant.security.UserPrincipal;
import com.example.italianrestaurant.user.User;
import com.example.italianrestaurant.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final DeliveryService deliveryService;
    private final ModelMapper modelMapper;
    private final MealOrderService mealOrderService;
    private final UserService userService;

    public List<Order> getOrdersByUserEmail(UserPrincipal userPrincipal) {
        return orderRepository.findAllByUserEmail(userPrincipal.getEmail());
    }

    public List<Order> getAllOrdersFromToday() {
        return orderRepository.findAllFromToday(LocalDateTime.now().toLocalDate());
    }

    public Order makeOrder(UserPrincipal userPrincipal, OrderDto orderDto) {
        Delivery dbDelivery = deliveryService.addDelivery(orderDto.getDelivery());
        User user = userService.getUserByEmail(userPrincipal.getEmail());
        Order order = Order.builder()
                .delivery(dbDelivery)
                .orderDate(LocalDateTime.now())
                .user(user)
                .orderStatus(orderDto.getOrderStatus())
                .build();

        Order savedOrder = orderRepository.save(order);

        List<MealOrder> mealOrders = orderDto.getMealOrders().stream()
                .map(mealOrderDto -> modelMapper.map(mealOrderDto, MealOrder.class))
                .toList();

        mealOrders.forEach(mealOrder -> mealOrder.setOrder(savedOrder));
        List<MealOrder> dbMealOrders = mealOrders.stream().map(mealOrderService::addMealOrder).toList();
        savedOrder.setMealOrders(dbMealOrders);
        return savedOrder;
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
