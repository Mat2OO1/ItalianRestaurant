package com.example.italianrestaurant.exceptions;

public class InvalidTokenException extends Exception {
    public InvalidTokenException() {
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
