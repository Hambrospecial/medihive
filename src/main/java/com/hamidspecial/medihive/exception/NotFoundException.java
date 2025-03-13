package com.hamidspecial.medihive.exception;


public class NotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;
    private String code;
    public NotFoundException(String code, String message) {
        super(message);
        this.code = code;
    }
}
