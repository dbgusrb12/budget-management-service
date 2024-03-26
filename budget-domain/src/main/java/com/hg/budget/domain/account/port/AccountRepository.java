package com.hg.budget.domain.account.port;

import com.hg.budget.domain.account.Account;

public interface AccountRepository {

    Account save(Account account);

    Account findById(String id);
}
