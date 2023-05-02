package com.example.italianrestaurant.exceptions;

public class InvalidEntityException extends Exception {
    public InvalidEntityException() {
    }

    public InvalidEntityException(String message) {
        super(message);
    }

    public InvalidEntityException(Boolean aBoolean) {

    }
}
