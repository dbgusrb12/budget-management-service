package com.hg.budget.domain.account;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountRole {
    USER("ROLE_USER"),
    MANAGER("ROLE_MANAGER"),
    ADMIN("ROLE_ADMIN"),
    UNKNOWN("UNKNOWN");

    private final String value;

    public static AccountRole of(String value) {
        return Arrays.stream(values())
            .filter(name -> name.value.equals(value))
            .findAny()
            .orElse(UNKNOWN);
    }
}
