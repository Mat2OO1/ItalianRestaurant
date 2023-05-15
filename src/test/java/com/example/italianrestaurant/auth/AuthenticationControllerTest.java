package com.example.italianrestaurant.auth;

import com.example.italianrestaurant.Utils;
import com.example.italianrestaurant.security.JwtAuthenticationFilter;
import jakarta.persistence.EntityExistsException;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthenticationController.class,
        excludeFilters =
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    void shouldAuthenticate() throws Exception {

        // given
        val authenticationRequest = AuthenticationRequest.builder()
                .email("email@email.com")
                .password("password")
                .build();

        val authenticationResponse = AuthenticationResponse.builder()
                .token("token")
                .build();

        given(authenticationService.authenticate(authenticationRequest)).willReturn(authenticationResponse);

        // when
        val resultActions = mockMvc.perform(post("/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.objectToJsonString(authenticationRequest)));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void shouldNotAuthenticate() throws Exception {

        // given
        val authenticationRequest = AuthenticationRequest.builder()
                .email("email@email.com")
                .password("password")
                .build();


        given(authenticationService.authenticate(authenticationRequest))
                .willThrow(BadCredentialsException.class);

        // when
        val resultActions = mockMvc.perform(post("/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.objectToJsonString(authenticationRequest)));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    void shouldRegister() throws Exception {

        // given
        val registerRequest = RegisterRequest.builder()
                .email("example@email.com")
                .password("password")
                .firstname("firstname")
                .lastname("lastname")
                .build();

        val authenticationResponse = AuthenticationResponse.builder()
                .token("token")
                .build();

        doNothing().when(authenticationService).register(registerRequest);

        // when
        val resultActions = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.objectToJsonString(registerRequest)));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    void shouldNotRegister() throws Exception {

        // given
        val registerRequest = RegisterRequest.builder()
                .email("email")
                .password("password")
                .firstname("firstname")
                .lastname("lastname")
                .build();

        doThrow(EntityExistsException.class).when(authenticationService).register(registerRequest);

        // when
        val resultActions = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Utils.objectToJsonString(registerRequest)));

        // then
        resultActions.andExpect(status().isBadRequest());
    }


}
