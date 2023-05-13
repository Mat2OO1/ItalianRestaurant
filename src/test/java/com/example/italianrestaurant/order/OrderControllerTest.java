package com.example.italianrestaurant.order;

import com.example.italianrestaurant.Utils;
import com.example.italianrestaurant.config.security.JwtAuthenticationFilter;
import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class,
        excludeFilters =
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @Test
    void shouldGetAllOrdersFromToday() throws Exception {
        //given
        val orders = Utils.buildOrders();
        given(orderService.getAllOrdersFromToday()).willReturn(orders);

        // when
        val resultActions = mockMvc.perform(get("/order/all")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        List<Order> orderList = Utils.jsonStringToObject(contentAsString, List.class);
        assertThat(orderList).hasSize(2);
    }

    @Test
    void shouldGetOrdersByUsers() throws Exception {
        //given
        val orders = Utils.buildOrders();
        given(orderService.getOrdersByUserEmail(any())).willReturn(List.of(orders.get(0)));

        // when
        val resultActions = mockMvc.perform(get("/order/user")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        List<Order> orderList = Utils.jsonStringToObject(contentAsString, List.class);
        assertThat(orderList).hasSize(1);
    }

    @Test
    void shouldMakeOrder() throws Exception {
        //given
        val orderDto = Utils.getOrderDto();
        val order = Utils.getOrder();

        given(orderService.makeOrder(any(), eq(orderDto))).willReturn(order);

        // when
        val resultActions = mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.objectToJsonString(orderDto)));

        // then
        resultActions.andExpect(status().isOk());
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        val resultOrder = Utils.jsonStringToObject(contentAsString, Order.class);
        assertThat(resultOrder).isEqualTo(order);
    }

    @Test
    void shouldChangeStatus() throws Exception {
        //given
        val changeStatusDto = Utils.getChangeOrderStatusDto();
        val order = Utils.getOrder();

        given(orderService.changeStatus(changeStatusDto)).willReturn(order);

        // when
        val resultActions = mockMvc.perform(post("/order/change-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.objectToJsonString(changeStatusDto)));

        // then
        resultActions.andExpect(status().isOk());
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        val resultOrder = Utils.jsonStringToObject(contentAsString, Order.class);
        assertThat(changeStatusDto.getOrderStatus()).isEqualTo(resultOrder.getOrderStatus());
    }

    @Test
    void shouldNotChangeStatus() throws Exception {
        //given
        val changeStatusDto = Utils.getChangeOrderStatusDto();

        given(orderService.changeStatus(changeStatusDto)).willThrow(new EntityNotFoundException());

        // when
        val resultActions = mockMvc.perform(post("/order/change-status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.objectToJsonString(changeStatusDto)));

        // then
        resultActions.andExpect(status().isNotFound());
    }
}
