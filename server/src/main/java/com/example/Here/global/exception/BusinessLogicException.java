package com.example.Here.global.exception;

import lombok.Getter;

@Getter
public class BusinessLogicException extends RuntimeException {

    private ExcepotionCode exceptionCode;

    public BusinessLogicException(ExcepotionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

}
