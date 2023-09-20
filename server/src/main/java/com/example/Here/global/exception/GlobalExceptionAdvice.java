package com.example.Here.global.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(BusinessLogicException.class)
    public ErrorResponseToBusinessLogicException handleBusinessLogicException(BusinessLogicException ex, HttpServletResponse response) {

        response.setStatus(ex.getExceptionCode().getStatus());
        return new ErrorResponseToBusinessLogicException(ex.getExceptionCode());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(DataAccessException e) {

        return new ResponseEntity<>("데이터베이스 오류", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {

        return new ResponseEntity<>("서버 오류", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SecurityAuthException.class)
    public ErrorResponseToSecurityAuthException handleSecurityAuthException(SecurityAuthException ex, HttpServletResponse response) {

        response.setStatus(ex.getSecurityAuthExceptionCode().getStatus());
        return new ErrorResponseToSecurityAuthException(ex.getSecurityAuthExceptionCode());
    }

    @ExceptionHandler(InvalidJsonFormatException.class)
    public ResponseEntity<String> handleInvalidJsonFormatException(InvalidJsonFormatException e) {
        return new ResponseEntity<>("Invalid JSON format", HttpStatus.BAD_REQUEST);
    }

}
