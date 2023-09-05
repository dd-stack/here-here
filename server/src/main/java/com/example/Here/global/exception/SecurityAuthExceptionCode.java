package com.example.Here.global.exception;

import lombok.Getter;

@Getter
public enum SecurityAuthExceptionCode {

    MEMBER_LOGOUT(401, "로그아웃 또는 회원 탈퇴한 토큰 입니다."),

    TOKEN_EXPIRED(401, "만료 된 토큰입니다"),

    KAKAO_ACCESS_TOKEN_NOT_FOUND(401, "카카오 토큰을 찾을 수 없습니다."),

    KAKAO_REFRESH_TOKEN_NOT_FOUND(401, "카카오 리프레시 토큰을 찾을 수 없습니다."),

    MEMBER_NO_PERMISSION(403, "인가되지 않은 사용자 입니다"),

    MEMBER_NOT_FOUND(404, "회원을 찾을 수 없습니다."),

    EMAIL_NOT_FOUND(404, "이메일을 찾을 수 없습니다."),

    KAKAO_USER_NOT_FOUND(404, "카카오 유저를 찾을 수 없습니다."),

    REFRESH_TOKEN_NOT_FOUND(406, "만료 된 리프레시 토큰입니다. 다시 로그인해주세요.");

    private int status;

    @Getter
    private String message;

    SecurityAuthExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
