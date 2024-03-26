package com.hg.budget.domain.account;

import com.hg.budget.core.util.TimeUtils;
import com.hg.budget.domain.account.port.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account createAccount(String id, String password, String nickname) {
        final Account duplicatedAccount = findAccount(id);
        if (duplicatedAccount.exist()) {
            return duplicatedAccount;
        }
        final Account account = Account.ofCreated(id, password, nickname, AccountRole.USER);
        accountRepository.save(account);
        return account;
    }

    public Account findAccount(String id) {
        return accountRepository.findById(id);
    }

    public Account login(String id) {
        final Account loginUser = findAccount(id);
        if (loginUser.notExist()) {
            return loginUser;
        }
        return accountRepository.save(loginUser.login(TimeUtils.now()));
    }
}
