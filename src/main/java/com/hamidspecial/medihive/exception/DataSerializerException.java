package com.hamidspecial.medihive.exception;

public class DataSerializerException extends RuntimeException {
    public DataSerializerException(String message) {
        super(message);
    }

    public DataSerializerException(String message, Throwable cause) {
        super(message, cause);
    }
}
