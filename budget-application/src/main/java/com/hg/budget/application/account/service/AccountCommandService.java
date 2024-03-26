package com.hg.budget.application.account.service;

import com.hg.budget.application.core.code.ApplicationCode;
import com.hg.budget.application.core.exception.ApplicationException;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountCommandService {

    private final AccountService accountService;

    @Transactional
    public void createAccount(String id, String password, String nickname) {
        final Account account = accountService.createAccount(id, password, nickname);
        if (account.isDuplicated()) {
            throw new ApplicationException(ApplicationCode.DUPLICATED_ACCOUNT);
        }
    }
}
