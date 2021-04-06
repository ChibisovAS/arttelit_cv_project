package com.arttelit.chibisov.simplestock.exceptions;

public class ForbiddenException extends Exception {

    public ForbiddenException() {
    }

    public ForbiddenException(String message) {
        super(message);
    }
}
