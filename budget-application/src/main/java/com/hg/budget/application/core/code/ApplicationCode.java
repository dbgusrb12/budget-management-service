package com.hg.budget.application.core.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public enum ApplicationCode {

    SUCCESS("0000", "성공하였습니다."),
    UNKNOWN_SERVER_ERROR("1000", "처리 중 오류가 발생하였습니다."),
    BAD_REQUEST("2000", "잘못된 요청입니다."),
    DUPLICATED_ACCOUNT("2001", "이미 존재하는 ID 입니다."),
    INVALID_AUTHENTICATION_INFO("2002", "인증 정보가 유효하지 않습니다."),
    BAD_CREDENTIALS("2003", "계정 비밀번호가 일치하지 않습니다."),
    ACCESS_DENIED_AUTHENTICATION("2004", "접근 권한이 없는 사용자입니다.");

    private final String code;
    private final String message;
}
