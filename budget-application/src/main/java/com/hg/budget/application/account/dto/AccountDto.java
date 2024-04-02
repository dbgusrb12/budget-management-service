package com.hg.budget.application.account.dto;

public record AccountDto(
    String id,
    String password,
    String nickname,
    String status,
    String role,
    String signUpDateTime,
    String signInDateTime
) {

}
