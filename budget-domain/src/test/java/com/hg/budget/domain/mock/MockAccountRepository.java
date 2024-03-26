package com.hg.budget.domain.mock;

import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.account.port.AccountRepository;
import java.util.ArrayList;
import java.util.List;

public class MockAccountRepository implements AccountRepository {

    private final List<Account> accountList = new ArrayList<>();

    @Override
    public void save(Account account) {
        final var saved = Account.of(
            account.getId(),
            account.getPassword(),
            account.getNickname(),
            account.getStatus().name(),
            account.getRole().getValue(),
            account.getSignUpDateTime(),
            account.getSignInDateTime()
        );
        accountList.add(saved);
    }

    @Override
    public Account findById(String id) {
        return accountList.stream()
            .filter(account -> account.getId().equals(id))
            .findFirst()
            .orElse(Account.ofNotExist());
    }

    @Override
    public void update(Account account) {
        accountList.remove(account);
        final var saved = Account.of(
            account.getId(),
            account.getPassword(),
            account.getNickname(),
            account.getStatus().name(),
            account.getRole().getValue(),
            account.getSignUpDateTime(),
            account.getSignInDateTime()
        );
        accountList.add(saved);
    }
}
