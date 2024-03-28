package com.hg.budget.was.core.security;

import com.hg.budget.application.account.AccountQueryService;
import com.hg.budget.application.account.dto.AccountDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserDetailsService {

    private final AccountQueryService accountQueryService;

    public UserDetails loadUserByUsername(String username) {
        final AccountDto account = accountQueryService.getAccount(username);
        return new UserDetails(account.getId(), account.getRole(), account.getPassword());
    }
}
