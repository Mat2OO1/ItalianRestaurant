package com.example.italianrestaurant.delivery;

import com.example.italianrestaurant.exceptions.InvalidEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<?> addDelivery(DeliveryDto delivery) {
        try {
            return ResponseEntity.ok(deliveryService.addDelivery(delivery));
        } catch (InvalidEntityException e) {
            return ResponseEntity.badRequest().body("Invalid delivery entity");
        }
    }
}
