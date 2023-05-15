package com.example.italianrestaurant.order;

import com.example.italianrestaurant.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrdersFromToday());
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> getOrderByUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.getOrdersByUserEmail(user));
    }

    @PostMapping
    public ResponseEntity<?> makeOrder(@AuthenticationPrincipal User user, @Valid @RequestBody OrderDto orderDto) {
        return ResponseEntity.ok(orderService.makeOrder(user, orderDto));

    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/change-status")
    public ResponseEntity<Order> changeStatus(@Valid @RequestBody ChangeOrderStatusDto orderDto) {
        try {
            return ResponseEntity.ok(orderService.changeStatus(orderDto));
        }
        catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "There is no order with id: " + orderDto.getOrderId(), e);
        }
    }
}
