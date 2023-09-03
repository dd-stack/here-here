package com.example.Here.global.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ErrorResponseToBusinessLogicException {

    private final int status;

    private final String error;

    private final String message;

    private final String timeStamp;

    public ErrorResponseToBusinessLogicException(ExceptionCode exceptionCode) {

        this.status = exceptionCode.getStatus();
        this.error = exceptionCode.name();
        this.message = exceptionCode.getMessage();
        this.timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
