package com.sebcode.msproducts.exception;

public class BadRequestException extends ApiException {
    public BadRequestException(String message) {
        super(message, "BAD_REQUEST");
    }
}
