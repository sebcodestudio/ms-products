package com.sebcode.msproducts.exception;

public class DuplicateResourceException extends ApiException {
    public DuplicateResourceException(String message) {
        super(message, "DUPLICATE_RESOURCE");
    }

    public DuplicateResourceException(String message, Throwable cause) {
        super(message, "DUPLICATE_RESOURCE", cause);
    }
}
