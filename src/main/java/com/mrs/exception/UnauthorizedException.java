package com.mrs.exception;

public class UnauthorizedException extends RuntimeException {
    private final Object object;

    public UnauthorizedException(String message, Object object) {
        super(message);
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}
