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
@Transactional(readOnly = true)
public class AccountQueryService {

    private final DateTimeHolder dateTimeHolder;
    private final AccountService accountService;

    public AccountDto getAccount(String id) {
        final Account account = accountService.findAccount(id);
        if (account.notExist()) {
            throw new ApplicationException(ApplicationCode.BAD_REQUEST, "유저가 존재하지 않습니다.");
        }

        return AccountDto.of(account, dateTimeHolder);
    }
}
