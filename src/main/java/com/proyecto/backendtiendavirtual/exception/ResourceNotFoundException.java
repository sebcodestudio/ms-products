package com.proyecto.backendtiendavirtual.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException (String message) {
        super(message);
    }
}