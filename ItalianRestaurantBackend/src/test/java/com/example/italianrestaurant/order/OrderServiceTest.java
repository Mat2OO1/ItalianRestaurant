package com.example.italianrestaurant.order;

import com.example.italianrestaurant.Utils;
import com.example.italianrestaurant.delivery.DeliveryService;
import com.example.italianrestaurant.exceptions.InvalidEntityException;
import com.example.italianrestaurant.order.mealorder.MealOrder;
import com.example.italianrestaurant.order.mealorder.MealOrderService;
import com.example.italianrestaurant.payments.OrderPaidResponse;
import com.example.italianrestaurant.payments.PaymentService;
import com.example.italianrestaurant.security.UserPrincipal;
import com.example.italianrestaurant.user.UserService;
import com.stripe.exception.StripeException;
import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private DeliveryService deliveryService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private MealOrderService mealOrderService;
    @Mock
    private UserService userService;
    @Mock
    private PaymentService paymentService;
    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldGetAllOrdersFromToday() {
        //given
        List<Order> orders = Utils.buildOrders();

        given(orderRepository.findAllFromTodayAndPaymentPaid(any(), eq(true))).willReturn(orders);

        //when
        val returnedOrders = orderService.getAllOrdersFromToday();

        //then
        assertThat(returnedOrders).isEqualTo(orders);
    }

    @Test
    void shouldNotGetAllOrders() {
        //given
        given(orderRepository.findAllFromTodayAndPaymentPaid(any(), eq(true))).willReturn(List.of());

        //when
        val returnedOrders = orderService.getAllOrdersFromToday();

        //then
        assertThat(returnedOrders).isEmpty();
    }

    @Test
    void shouldGetOrdersByUser() {
        //given
        val orders = Utils.buildOrders();
        val user = Utils.getUser();
        user.setId(1L);
        val userPrincipal = UserPrincipal.create(user);
        given(orderRepository.findAllByUserEmailAndPaymentPaid(user.getEmail(), true)).willReturn(List.of(orders.get(0)));

        //when
        val ordersByUser = orderService.getOrdersByUserEmail(userPrincipal);

        //then
        assertThat(ordersByUser).isNotEmpty().hasSize(1);
        assertThat(ordersByUser.get(0).getUser().getId()).isEqualTo(1);
    }

    @Test
    void shouldNotGetOrderByUser() {
        //given
        val user = Utils.getUser();
        user.setId(1L);
        val userPrincipal = UserPrincipal.create(user);
        given(orderRepository.findAllByUserEmailAndPaymentPaid(user.getEmail(), true)).willReturn(List.of());

        //when
        val ordersByUser = orderService.getOrdersByUserEmail(userPrincipal);

        //then
        assertThat(ordersByUser).isEmpty();
    }

    @Test
    void shouldMakeOrder() throws StripeException {
        //given
        val delivery = Utils.getDelivery();
        delivery.setId(1L);
        val meal = Utils.getMeal();
        val mealOrder = Utils.getMealOrder();
        mealOrder.setId(1L);
        mealOrder.setMeal(meal);
        val user = Utils.getUser();
        user.setId(1L);
        val orderPaidResponse = Utils.getOrderPaidResponse();

        val order = Order.builder()
                .id(1L)
                .user(user)
                .delivery(delivery)
                .mealOrders(List.of(mealOrder))
                .orderStatus(OrderStatus.IN_DELIVERY)
                .orderDate(LocalDateTime.now())
                .build();

        val userPrincipal = UserPrincipal.create(user);

        given(deliveryService.addDelivery(any())).willReturn(delivery);
        given(modelMapper.map(any(), eq(MealOrder.class))).willReturn(mealOrder);
        given(orderRepository.save(any())).willReturn(order);
        given(mealOrderService.addMealOrder(any())).willReturn(mealOrder);
        given(userService.getUserByEmail(any())).willReturn(user);
        given(paymentService.payment(any(), eq(order.getId()), any(), any())).willReturn(orderPaidResponse);
        given(paymentService.getPaymentBySessionId(any())).willReturn(Utils.getPayment());

        //when
        OrderPaidResponse result = orderService.makeOrder(userPrincipal, Utils.getOrderDto());

        //then
        assertThat(result).isNotNull();
        verify(orderRepository, times(2)).save(any());
    }

    @Test
    void shouldNotMakeOrderDeliveryNotValid() throws InvalidEntityException {
        //given
        val user = Utils.getUser();
        val orderDto = Utils.getOrderDto();
        val userPrincipal = UserPrincipal.create(user);
        given(deliveryService.addDelivery(any())).willThrow(InvalidEntityException.class);

        //when
        assertThatThrownBy(() -> orderService.makeOrder(userPrincipal, orderDto))
                .isInstanceOf(InvalidEntityException.class);

        //then
        verify(orderRepository, times(0)).save(any());
    }

    @Test
    void shouldChangeStatus() {
        //given
        val changeStatusDto = Utils.getChangeOrderStatusDto();
        val order = Utils.getOrder();
        order.setDelivery(Utils.getDelivery());
        order.setOrderStatus(OrderStatus.IN_DELIVERY);

        given(orderRepository.findById(changeStatusDto.getOrderId())).willReturn(Optional.of(order));
        given(orderRepository.save(any())).willReturn(order);

        //when
        Order result = orderService.changeStatus(changeStatusDto);

        //then
        assertThat(result.getOrderStatus()).isEqualTo(changeStatusDto.getOrderStatus());
    }

    @Test
    void shouldNotChangeStatus() {
        //given
        val changeStatusDto = Utils.getChangeOrderStatusDto();
        given(orderRepository.findById(changeStatusDto.getOrderId())).willReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> orderService.changeStatus(changeStatusDto))
                .isInstanceOf(EntityNotFoundException.class);

        //then
        verify(orderRepository, times(0)).save(any());
    }

    @Test
    void shouldDeleteOrderById() {
        //given
        val id = 1L;
        doNothing().when(orderRepository).deleteById(id);

        //when
        orderService.deleteOrderById(id);

        //then
        verify(orderRepository).deleteById(id);
    }

    @Test
    void shouldNotDeleteOrderById() {
        //given
        val id = 1L;
        doThrow(new EmptyResultDataAccessException(0)).when(orderRepository).deleteById(id);

        //when
        //then
        assertThatThrownBy(() -> orderService.deleteOrderById(id))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

}
