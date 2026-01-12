package com.proyecto.backendtiendavirtual.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }

}
