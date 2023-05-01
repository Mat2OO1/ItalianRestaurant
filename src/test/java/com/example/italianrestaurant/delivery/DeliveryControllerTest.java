package com.example.italianrestaurant.delivery;

import com.example.italianrestaurant.Utils;
import com.example.italianrestaurant.config.security.JwtAuthenticationFilter;
import com.example.italianrestaurant.exceptions.InvalidEntityException;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DeliveryController.class,
        excludeFilters =
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
public class DeliveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryService deliveryService;


    @Test
    void shouldAddDelivery() throws Exception {

        // given
        val deliveryDto = Utils.getDeliveryDto();
        val dbDelivery = Utils.getDelivery();
        dbDelivery.setId(1L);
        given(deliveryService.addDelivery(deliveryDto)).willReturn(dbDelivery);

        // when
        val resultActions = mockMvc.perform(post("/delivery")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.asJsonString(deliveryDto)));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void shouldNotAddDelivery() throws Exception {

        // given
        val deliveryDto = new DeliveryDto();
        given(deliveryService.addDelivery(deliveryDto)).willThrow(new InvalidEntityException("Invalid delivery entity"));

        // when
        val resultActions = mockMvc.perform(post("/delivery")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.asJsonString(deliveryDto)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }
}
