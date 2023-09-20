package com.example.Here.global.exception;

import lombok.Getter;

public enum ExceptionCode {

    NOT_VALID_REQUEST(400, "잘못된 요청입니다."),

    IMAGE_URL_NOT_VALID(401, "유효하지 않은 이미지 URL입니다."),

    NO_PERMISSION_FOR_CALENDAR(403, "톡캘린더 api호출 권한이 없습니다."),

    NO_PERMISSION_FOR_CHECKING_MEMBER(403, "수락한 명단 조회 권한이 없습니다."),

    MEMBER_NOT_FOUND(404, "회원이 존재하지 않습니다."),

    CARD_NOT_FOUND(404, "초대장이 존재하지 않습니다."),

    INVITATION_NOT_FOUND(404, "초대를 찾을 수 없습니다."),

    ALREADY_ACCEPTED(409, "이미 수락한 초대장 입니다."),

    FILE_SIZE_EXCEEDED(417, "파일 사이즈가 초과되었습니다."),

    FILE_UPLOAD_FAILED(500, "파일 업로드에 실패했습니다.");


    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}
