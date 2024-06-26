package com.hg.budget.domain.account;

import com.hg.budget.core.client.DateTimeHolder;
import com.hg.budget.domain.account.port.AccountRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final DateTimeHolder dateTimeHolder;
    private final AccountRepository accountRepository;

    @Transactional
    public Account createAccount(String id, String password, String nickname) {
        final Account duplicatedAccount = findAccount(id);
        if (duplicatedAccount.exist()) {
            return duplicatedAccount;
        }
        final Account account = Account.ofCreated(id, password, nickname, AccountRole.USER, dateTimeHolder);
        accountRepository.save(account);
        return account;
    }

    public Account findAccount(String id) {
        return accountRepository.findById(id);
    }

    @Transactional
    public Account login(String id) {
        final Account loginUser = findAccount(id);
        if (loginUser.notExist()) {
            return loginUser;
        }
        final Account login = loginUser.login(dateTimeHolder);
        accountRepository.update(login);
        return login;
    }

    public List<Account> findAccounts() {
        return accountRepository.findAll();
    }
}
