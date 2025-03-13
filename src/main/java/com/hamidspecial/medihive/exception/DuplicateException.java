package com.hamidspecial.medihive.exception;

public class DuplicateException extends RuntimeException{
    private String code;
    public DuplicateException(String code, String message){
        super(message);
        this.code = code;
    }
}
