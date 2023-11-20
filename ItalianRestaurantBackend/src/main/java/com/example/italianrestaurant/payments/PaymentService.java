package com.example.italianrestaurant.payments;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

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

    public OrderPaidResponse payment(List<PaymentRequest> paymentList, long orderId, String email) throws StripeException {

        Stripe.apiKey = secretKey;
        SessionCreateParams params = SessionCreateParams.builder()
                .setCustomerEmail(email)
                .setLocale(SessionCreateParams.Locale.EN)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.BLIK)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.P24)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.PAYPAL)
                .setMode(SessionCreateParams.Mode.PAYMENT).setSuccessUrl(frontUrl + "/confirmation/" + orderId)
                .setCancelUrl(frontUrl + "/menu?payment=failed")
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
                .amount(session.getAmountTotal())
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);
        return new OrderPaidResponse(session.getId(), session.getUrl());
    }

    public void updatePayment(String payload, String sigHeader) throws StripeException {
        Event event;
        event = Webhook.constructEvent(
                payload, sigHeader, webhookSecret);
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

        switch (event.getType()) {
            case "checkout.session.completed": {

                Session sessionObject;
                if (dataObjectDeserializer.getObject().isPresent()) {
                    sessionObject = (Session) dataObjectDeserializer.getObject().get();
                } else {
                    throw new RuntimeException("Unable to deserialize event: " + payload);
                }

                Payment payment = paymentRepository.findBySessionId(sessionObject.getId());
                payment.setPaid(true);
                payment.setAmount(sessionObject.getAmountTotal());
                payment.setCreatedAt(LocalDateTime.now());
                paymentRepository.save(payment);
                break;
            }
            default:
                System.out.println("Unhandled event type: " + event.getType());
        }

    }

    public Payment getPaymentBySessionId(String sessionId) {
        return paymentRepository.findBySessionId(sessionId);
    }

    private Long getCentsFromDollars(double dollars) {
        return (long) (dollars * 100);
    }
}
