package com.example.italianrestaurant.passwordreset;

import com.example.italianrestaurant.Utils;
import com.example.italianrestaurant.passwordreset.passwordtoken.PasswordToken;
import com.example.italianrestaurant.security.JwtAuthenticationFilter;
import jakarta.mail.MessagingException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PasswordResetController.class,
        excludeFilters =
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
public class PasswordResetControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PasswordResetService passwordResetService;

    @Test
    void shouldSendResetRequest() throws Exception {
        //given
        val email = "email";
        val passwordToken = Utils.getPasswordToken();
        val user = Utils.getUser();
        passwordToken.setUser(user);
        given(passwordResetService.sendResetPasswordRequest(any(), any())).willReturn(passwordToken);

        //when
        val resultActions = mockMvc.perform(get("/password/request")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", email));

        //then
        resultActions.andExpect(status().isOk());
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        val resultPasswordToken = Utils.jsonStringToObject(contentAsString, PasswordToken.class);
        assertThat(resultPasswordToken).isEqualTo(passwordToken);

    }

    @Test
    void shouldNotSendResetRequest() throws Exception {
        //given
        val email = "email";
        val lang = "en";
        given(passwordResetService.sendResetPasswordRequest(email, lang)).willThrow(new MessagingException());

        // when
        val resultActions = mockMvc.perform(get("/password/request")
                .param("email", email));

        // then
        resultActions.andExpect(status().isBadGateway());
    }

    @Test
    void shouldNotFindUserAndNotSendPasswordRequest() throws Exception {
        //given
        val email = "email";
        val lang = "en";
        given(passwordResetService.sendResetPasswordRequest(email, lang)).willThrow(new EntityNotFoundException());

        // when
        val resultActions = mockMvc.perform(get("/password/request")
                .param("email", email));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void shouldResetPassword() throws Exception {
        //given
        val passwordResetRequest = Utils.getPasswordResetRequest();
        doNothing().when(passwordResetService).resetPassword(passwordResetRequest);

        //when
        val resultActions = mockMvc.perform(post("/password/reset")
                .content(Utils.objectToJsonString(passwordResetRequest))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk());
    }
}
