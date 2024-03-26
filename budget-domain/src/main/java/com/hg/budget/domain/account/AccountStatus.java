package com.hg.budget.domain.account;

import java.util.Arrays;

public enum AccountStatus {
    CREATED,    // 회원가입 성공
    LIVED,      // 유효
    LEFT,       // 탈퇴
    UNKNOWN;

    public static AccountStatus of(String value) {
        return Arrays.stream(values())
            .filter(status -> status.name().equals(value))
            .findAny()
            .orElse(UNKNOWN);
    }
}

