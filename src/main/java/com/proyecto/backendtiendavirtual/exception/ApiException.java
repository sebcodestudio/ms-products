package com.proyecto.backendtiendavirtual.exception;

public abstract class ApiException extends RuntimeException {
    private String code;

    public ApiException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
