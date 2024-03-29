package com.example.italianrestaurant.email;

import com.example.italianrestaurant.order.mealorder.MealOrder;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    public void sendMessage(EmailEntity email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email.getFrom());
        message.setTo(email.getTo());
        message.setSubject(email.getSubject());
        message.setText(email.getText());
        emailSender.send(message);
    }


    public void sendHtmlMessage(EmailEntity email) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariables(email.getProperties());
        helper.setFrom(email.getFrom());
        helper.setTo(email.getTo());
        helper.setSubject(email.getSubject());
        String html = templateEngine.process(email.getTemplate(), context);
        helper.setText(html, true);

        emailSender.send(message);
    }

    public EmailEntity buildPasswordResetEmail(String to, String url, String lang) {

        String template =  lang.equals("pl") ? "password-reset-pl.html" : "password-reset.html";

        EmailEntity email = EmailEntity.builder()
                .to(to)
                .from("ladolcevita@rest.com")
                .subject("Password reset request")
                .template(template)
                .build();

        email.addProperty("email", to);
        email.addProperty("url", url);
        return email;
    }

    public EmailEntity buildOrderConfirmationEmail(String to, String name, List<MealOrder> mealOrders, String confirmationLink, String lang) {
        String template =  lang.equals("pl") ? "invoicepl.html" : "invoice.html";
        EmailEntity email = EmailEntity.builder()
                .to(to)
                .from("ladolcevita@rest.com")
                .subject("Order confirmation")
                .template(template)
                .build();

        List<MealOrderEmail> mealOrderEmails = mealOrders.stream().map(mealOrder -> MealOrderEmail.builder()
                .mealName(mealOrder.getMeal().getName())
                .price(String.format("%.2f", mealOrder.getMeal().getPrice() * mealOrder.getQuantity()))
                .quantity(mealOrder.getQuantity())
                .build()).toList();

        email.addProperty("orders", mealOrderEmails);
        email.addProperty("name", name);
        email.addProperty("confirmationLink", confirmationLink);
        email.addProperty("totalPrice",
                String.format("%.2f", mealOrders.stream().mapToDouble(mealOrder -> mealOrder.getMeal().getPrice() * mealOrder.getQuantity()).sum()));
        return email;
    }
}
