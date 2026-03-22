package com.sebcode.msproducts.exception;

public class ForbiddenException extends ApiException {
    public ForbiddenException(String message) {
        super(message, "FORBIDDEN");
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, "FORBIDDEN");
        initCause(cause);
    }
}
