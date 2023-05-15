package com.example.italianrestaurant.order;

import com.example.italianrestaurant.Utils;
import com.example.italianrestaurant.delivery.DeliveryService;
import com.example.italianrestaurant.exceptions.InvalidEntityException;
import com.example.italianrestaurant.order.mealorder.MealOrder;
import com.example.italianrestaurant.order.mealorder.MealOrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldGetAllOrdersFromToday() {
        //given
        List<Order> orders = Utils.buildOrders();

        given(orderRepository.findAllFromToday(any())).willReturn(orders);

        //when
        val returnedOrders = orderService.getAllOrdersFromToday();

        //then
        assertThat(returnedOrders).isEqualTo(orders);
    }

    @Test
    void shouldNotGetAllOrders() {
        //given
        given(orderRepository.findAllFromToday(any())).willReturn(List.of());

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
        given(orderRepository.findAllByUserEmail(user.getEmail())).willReturn(List.of(orders.get(0)));

        //when
        val ordersByUser = orderService.getOrdersByUserEmail(user);

        //then
        assertThat(ordersByUser).isNotEmpty().hasSize(1);
        assertThat(ordersByUser.get(0).getUser().getId()).isEqualTo(1);
    }

    @Test
    void shouldNotGetOrderByUser() {
        //given
        val user = Utils.getUser();
        user.setId(1L);
        given(orderRepository.findAllByUserEmail(user.getEmail())).willReturn(List.of());

        //when
        val ordersByUser = orderService.getOrdersByUserEmail(user);

        //then
        assertThat(ordersByUser).isEmpty();
    }

    @Test
    void shouldMakeOrder() {
        //given
        val delivery = Utils.getDelivery();
        delivery.setId(1L);
        val mealOrder = Utils.getMealOrder();
        mealOrder.setId(1L);
        val user = Utils.getUser();
        user.setId(1L);

        val order = Order.builder()
                .id(1L)
                .user(user)
                .delivery(delivery)
                .mealOrders(List.of(mealOrder))
                .orderStatus(OrderStatus.IN_DELIVERY)
                .orderDate(LocalDateTime.now())
                .build();

        given(deliveryService.addDelivery(any())).willReturn(delivery);
        given(modelMapper.map(any(), eq(MealOrder.class))).willReturn(Utils.getMealOrder());
        given(orderRepository.save(any())).willReturn(order);
        given(mealOrderService.addMealOrder(any())).willReturn(mealOrder);

        //when
        Order result = orderService.makeOrder(user, Utils.getOrderDto());

        //then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(orderRepository).save(any());
    }

    @Test
    void shouldNotMakeOrderDeliveryNotValid() throws InvalidEntityException {
        //given
        val user = Utils.getUser();
        val orderDto = Utils.getOrderDto();
        given(deliveryService.addDelivery(any())).willThrow(InvalidEntityException.class);

        //when
        assertThatThrownBy(() -> orderService.makeOrder(user, orderDto))
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


}
