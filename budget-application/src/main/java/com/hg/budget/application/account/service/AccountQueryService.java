package com.hg.budget.application.account.service;

import com.hg.budget.application.account.service.dto.AccountDto;
import com.hg.budget.application.core.code.ApplicationCode;
import com.hg.budget.application.core.exception.ApplicationException;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountQueryService {

    private final AccountService accountService;

    @Transactional
    public AccountDto findAccount(String id) {
        final Account account = accountService.findAccount(id);
        if (account.notExist()) {
            throw new ApplicationException(ApplicationCode.BAD_REQUEST);
        }

        return new AccountDto(
            account.getId(),
            account.getPassword(),
            account.getNickname(),
            account.getSignUpDateTime(),
            account.getSignInDateTime()
        );
    }
}
