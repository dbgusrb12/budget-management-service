package com.hg.budget.application.account;

import com.hg.budget.application.account.dto.AccountDto;
import com.hg.budget.application.core.code.ApplicationCode;
import com.hg.budget.application.core.exception.ApplicationException;
import com.hg.budget.core.client.DateTimeHolder;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountQueryService {

    private final DateTimeHolder dateTimeHolder;
    private final AccountService accountService;

    @Transactional(readOnly = true)
    public AccountDto getAccount(String id) {
        final Account account = accountService.findAccount(id);
        if (account.notExist()) {
            throw new ApplicationException(ApplicationCode.BAD_REQUEST);
        }

        return new AccountDto(
            account.getId(),
            account.getPassword(),
            account.getNickname(),
            account.getStatus(),
            account.getRole(),
            dateTimeHolder.toString(account.getSignUpDateTime()),
            dateTimeHolder.toString(account.getSignInDateTime())
        );
    }
}
