package com.example.Here.global.exception;

public class InvalidJsonFormatException extends RuntimeException {

    public InvalidJsonFormatException(String message, Throwable cause) {
        super(message, cause);
    }

}
