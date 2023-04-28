package com.example.italianrestaurant.exceptions;

public class TokenNotFoundException extends Exception{
    public TokenNotFoundException() {
    }

    public TokenNotFoundException(String message) {
        super(message);
    }
}
