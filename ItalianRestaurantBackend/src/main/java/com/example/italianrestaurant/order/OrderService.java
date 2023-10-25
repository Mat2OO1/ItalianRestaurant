package com.example.italianrestaurant.order;

import com.example.italianrestaurant.aws.AwsService;
import com.example.italianrestaurant.delivery.Delivery;
import com.example.italianrestaurant.delivery.DeliveryService;
import com.example.italianrestaurant.order.mealorder.MealOrder;
import com.example.italianrestaurant.order.mealorder.MealOrderService;
import com.example.italianrestaurant.payments.PaymentRequest;
import com.example.italianrestaurant.payments.PaymentResponse;
import com.example.italianrestaurant.payments.PaymentService;
import com.example.italianrestaurant.security.UserPrincipal;
import com.example.italianrestaurant.user.User;
import com.example.italianrestaurant.user.UserService;
import com.stripe.exception.StripeException;
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
    private final PaymentService paymentService;
    private final AwsService awsService;

    public List<Order> getOrdersByUserEmail(UserPrincipal userPrincipal) {
        List<Order> allByUserEmail = orderRepository.findAllByUserEmail(userPrincipal.getEmail());
        allByUserEmail.forEach(order -> order.getMealOrders().forEach(mealOrder -> mealOrder.getMeal().setImage(awsService.getObjectUrl(mealOrder.getMeal().getImage()))));
        return allByUserEmail;
    }

    public List<Order> getAllOrdersFromToday() {
        return orderRepository.findAllFromToday(LocalDateTime.now().toLocalDate());
    }

    public PaymentResponse makeOrder(UserPrincipal userPrincipal, OrderDto orderDto) throws StripeException {
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
        mealOrders.forEach(mealOrderService::addMealOrder);

        return paymentService.payment(getPaymentRequestList(savedOrder), savedOrder.getId());
    }

    public Order changeStatus(ChangeOrderStatusDto orderDto) {
        Optional<Order> order = orderRepository.findById(orderDto.getOrderId());
        if (order.isEmpty())
            throw new EntityNotFoundException("Order with id " + orderDto.getOrderId() + " not found");

        order.get().setOrderStatus(orderDto.getOrderStatus());
        order.get().getDelivery().setDeliveryDate(orderDto.getDeliveryDate());
        return orderRepository.save(order.get());

    }
    public void deleteOrderById(Long id) {
        orderRepository.deleteById(id);
    }

    private List<PaymentRequest> getPaymentRequestList(Order order) {
        return order.getMealOrders().stream()
                .map(mealOrder -> PaymentRequest.builder()
                        .productName(mealOrder.getMeal().getName())
                        .price(mealOrder.getMeal().getPrice())
                        .quantity(mealOrder.getQuantity())
                        .build())
                .toList();
    }
}
