package com.example.italianrestaurant.order;

import com.example.italianrestaurant.delivery.Delivery;
import com.example.italianrestaurant.meal.Meal;
import com.example.italianrestaurant.security.UserPrincipal;
import com.stripe.exception.StripeException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Log4j2
public class  OrderController {

    private final OrderService orderService;

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrdersFromToday());
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> getOrderByUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(orderService.getOrdersByUserEmail(userPrincipal));
    }

    @GetMapping("/last")
    public ResponseEntity<Delivery> getLastDelivery(@AuthenticationPrincipal UserPrincipal userPrincipal){
        try{
            return ResponseEntity.ok(this.orderService.getLastDelivery(userPrincipal));
        } catch (EntityNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/order")
    public ResponseEntity<?> makeOrder(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody OrderDto orderDto,
            @RequestHeader(value = "lang", defaultValue = "en") String lang
    ) {
        try {
            return ResponseEntity.ok(orderService.makeOrder(userPrincipal, orderDto, lang));
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/change-status")
    public ResponseEntity<Order> changeStatus(@Valid @RequestBody ChangeOrderStatusDto orderDto) {
        try {
            return ResponseEntity.ok(orderService.changeStatus(orderDto));
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "There is no order with id: " + orderDto.getOrderId(), e);
        }
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Meal> deleteOrderById(@PathVariable Long id) {
        try {
            orderService.deleteOrderById(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "There is no order with id: " + id, e);
        }
    }
}
