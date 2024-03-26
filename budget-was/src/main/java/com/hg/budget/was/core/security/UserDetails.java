package com.hg.budget.was.core.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDetails {

    private final String id;
    private final String role;
    private String password;
}
