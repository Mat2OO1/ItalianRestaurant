package com.example.italianrestaurant.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    public void sendMessage(String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("italian.restaurant@rest.com");
        message.setTo(to);
        message.setSubject("Password reset");
        message.setText(text);
        emailSender.send(message);
    }
}
