package com.sebcode.msproducts.exception;

public abstract class ApiException extends RuntimeException {
    private String code;

    public ApiException(String message, String code) {
        super(message);
        this.code = code;
    }

    public ApiException(String message, String code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
