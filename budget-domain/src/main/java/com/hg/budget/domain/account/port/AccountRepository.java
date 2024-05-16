package com.hg.budget.domain.account.port;

import com.hg.budget.domain.account.Account;
import java.util.List;

public interface AccountRepository {

    void save(Account account);

    Account findById(String id);

    void update(Account account);

    List<Account> findAll();
}
