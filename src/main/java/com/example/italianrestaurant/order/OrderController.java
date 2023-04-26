package com.example.italianrestaurant.order;

import com.example.italianrestaurant.delivery.DeliveryDto;
import com.example.italianrestaurant.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> getOrderByUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.getOrdersByUser(user));
    }

    @PostMapping
    public ResponseEntity<Order> makeOrder(@AuthenticationPrincipal User user, @RequestBody DeliveryDto deliveryDto) {
        return ResponseEntity.ok(orderService.makeOrder(user, deliveryDto));
    }
}
