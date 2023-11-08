package com.example.italianrestaurant.table.reservation;

public class TableAlreadyReservedException extends RuntimeException {

    public TableAlreadyReservedException() {
        super("Table is already reserved for this date");
    }
}
