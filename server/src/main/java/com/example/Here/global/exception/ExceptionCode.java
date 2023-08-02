package com.example.Here.global.exception;

import lombok.Getter;

public enum ExceptionCode {

    TOKEN_NOT_VALID(401, "유효하지 않은 토큰입니다."),

    MEMBER_NO_PERMISSION(403, "인가되지 않은 사용자 입니다"),

    MEMBER_NOT_FOUND(404, "회원이 존재하지 않습니다."),

    CARD_NOT_FOUND(404, "초대장이 존재하지 않습니다."),

    MEMBER_NICKNAME_EXISTS(409, "이미 존재하는 닉네임 입니다."),

    MEMBER_EXISTS(409, "이미 존재하는 회원 입니다."),

    ALREADY_ACCEPTED(409, "이미 수락한 초대장 입니다."),
    INVITATION_NOT_FOUND(404, "초대장이 존재하지 않습니다.");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}
