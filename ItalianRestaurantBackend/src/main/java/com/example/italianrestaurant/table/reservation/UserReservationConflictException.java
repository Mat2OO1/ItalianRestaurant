package com.example.italianrestaurant.table.reservation;

public class UserReservationConflictException extends RuntimeException {

    public UserReservationConflictException() {
        super("User already has a reservation on this day");
    }
}
