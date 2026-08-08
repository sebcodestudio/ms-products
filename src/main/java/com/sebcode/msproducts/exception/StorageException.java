package com.sebcode.msproducts.exception;

public class StorageException extends ApiException {
    public StorageException(String message, Throwable cause) {
        super(message, "STORAGE_ERROR", cause);
    }
}
