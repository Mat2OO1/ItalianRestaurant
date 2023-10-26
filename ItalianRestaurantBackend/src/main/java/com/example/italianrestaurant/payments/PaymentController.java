package com.example.italianrestaurant.payments;

import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhookEvent(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {
        try {
            paymentService.updatePayment(payload, sigHeader);
            return ResponseEntity.ok().build();
        } catch (StripeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
