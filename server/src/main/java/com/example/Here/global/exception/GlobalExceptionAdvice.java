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
    public ErrorResponse handleBusinessLogicException(BusinessLogicException ex, HttpServletResponse response) {
        response.setStatus(ex.getExceptionCode().getStatus());
        return new ErrorResponse(ex.getExceptionCode());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(DataAccessException e) {
        return new ResponseEntity<>("데이터베이스 오류", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
