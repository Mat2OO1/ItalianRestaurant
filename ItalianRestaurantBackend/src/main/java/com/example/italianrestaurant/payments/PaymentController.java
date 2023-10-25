package com.example.italianrestaurant.payments;

import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


    @PostMapping("/webhook")
    public void handleWebhookEvent(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {
        try {
            paymentService.updateOrder(payload, sigHeader);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
}
