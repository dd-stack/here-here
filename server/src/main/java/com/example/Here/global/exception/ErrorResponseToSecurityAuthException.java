package com.example.Here.global.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class ErrorResponseToSecurityAuthException {

    private final int status;

    private final String error;

    private final String message;

    private final String timeStamp;

    public ErrorResponseToSecurityAuthException(SecurityAuthExceptionCode securityAuthExceptionCode) {
        this.status = securityAuthExceptionCode.getStatus();
        this.error = securityAuthExceptionCode.name();
        this.message = securityAuthExceptionCode.getMessage();
        this.timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
