package com.example.italianrestaurant.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring6.SpringTemplateEngine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @Spy
    private SpringTemplateEngine templateEngine;

    @InjectMocks
    private EmailService emailService;

    private EmailEntity email;

    @BeforeEach
    public void setUp() {
        email = EmailEntity.builder()
                .to("email@email.com")
                .from("ladolcevita@rest.com")
                .subject("Password reset request")
                .template("password-reset-pl.html")
                .build();

        email.addProperty("email", "email@email.com");
        email.addProperty("url", "http://localhost:8080/reset-password?token=1234567890");
    }

    @Test
    void shouldSendHtmlMessage() throws MessagingException {
        // given
        given(emailSender.createMimeMessage()).willReturn(new MimeMessage((jakarta.mail.Session) null));
        // when
        emailService.sendHtmlMessage(email);
        // then
        verify(emailSender).send((MimeMessage) any());
    }

    @Test
    void shouldNotSendHtmlMessage() {
        // given
        given(emailSender.createMimeMessage()).willReturn(new MimeMessage((jakarta.mail.Session) null));
        doThrow(new MailException("") {
        }).when(emailSender).send((MimeMessage) any());

        // when
        // then
        assertThatThrownBy(() -> emailService.sendHtmlMessage(email))
                .isInstanceOf(MailException.class);
    }

    @Test
    void shouldSendMessage() {
        //when
        emailService.sendMessage(email);
        //then
        verify(emailSender, times(1)).send((SimpleMailMessage) any());
    }

    @Test
    void shouldNotSendMessage() {
        //given
        doThrow(new MailException("") {
        }).when(emailSender).send((SimpleMailMessage) any());

        //when
        //then
        assertThatThrownBy(() -> emailService.sendMessage(email))
                .isInstanceOf(MailException.class);
    }

    @Test
    void shouldBuildEmail() throws MessagingException {
        //given
        val to = "email@email.com";
        val url = "http://localhost:8080/reset-password?token=1234567890";
        val lang = "pl";
        //when
        EmailEntity serviceEmail = emailService.buildPasswordResetEmail(to, url, lang);

        //then
        assertThat(serviceEmail).isEqualTo(email);
    }
}
