package com.mrs.exception;

public class NotFoundException extends RuntimeException {
    private Object object;

    public NotFoundException(String message, Object object) {
        super(message);
        this.object = object;
    }
}
