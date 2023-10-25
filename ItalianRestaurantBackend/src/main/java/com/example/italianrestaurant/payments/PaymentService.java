package com.example.italianrestaurant.payments;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${stripe.secret-key}")
    private String secretKey;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    @Value("${app.front-url}")
    private String frontUrl;

    private final PaymentRepository paymentRepository;

    public PaymentResponse payment(List<PaymentRequest> paymentList, long orderId) throws StripeException {

        Stripe.apiKey = secretKey;
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.BLIK)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.P24)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.PAYPAL)
                .setMode(SessionCreateParams.Mode.PAYMENT).setSuccessUrl(frontUrl + "/confirmation/" + orderId)
                .setCancelUrl(frontUrl + "/basket")
                .addAllLineItem(
                        paymentList.stream().map(paymentRequest -> SessionCreateParams.LineItem.builder()
                                .setQuantity(paymentRequest.getQuantity())
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("pln").setUnitAmount(getCentsFromDollars(paymentRequest.getPrice()))
                                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData
                                                        .builder().setName(paymentRequest.getProductName()).build())
                                                .build())
                                .build()).toList())
                .build();

        Session session = Session.create(params);

        Payment payment = Payment.builder()
                .sessionId(session.getId())
                .isPaid(false)
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);
        return new PaymentResponse(session.getId(), session.getUrl());
    }

    public void updateOrder(String payload, String sigHeader) throws StripeException {
        Event event;
        event = Webhook.constructEvent(
                payload, sigHeader, webhookSecret);

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        PaymentIntent paymentIntent;
        if (dataObjectDeserializer.getObject().isPresent()) {
            paymentIntent = (PaymentIntent) dataObjectDeserializer.getObject().get();
        } else {
            throw new RuntimeException("Unable to deserialize event: " + payload);
        }
        switch (event.getType()) {
            case "payment_intent.succeeded": {
                Payment payment = paymentRepository.findBySessionId(event.getId());
                payment.setPaid(true);
                payment.setAmount(paymentIntent.getAmount());
                payment.setPaymentType(paymentIntent.getPaymentMethodTypes().get(0));
                payment.setCreatedAt(LocalDateTime.now());
                break;
            }
            default:
                System.out.println("Unhandled event type: " + event.getType());
        }

    }

    private Long getCentsFromDollars(double dollars) {
        return (long) (dollars * 100);
    }
}
