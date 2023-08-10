package com.example.Here.global.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(BusinessLogicException.class)
    public ErrorResponse handleBusinessLogicException(BusinessLogicException ex, HttpServletResponse response) {
        response.setStatus(ex.getExceptionCode().getStatus());
        return new ErrorResponse(ex.getExceptionCode());
    }

}
