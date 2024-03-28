package com.hg.budget.domain.budget.port;

import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.budget.Budget;
import java.util.List;

public interface BudgetRepository {

    void save(Budget budget);

    void saveAll(List<Budget> budgets);

    void update(Budget budget);

    List<Budget> findAll();

    List<Budget> findByCreatedUser(Account account);
}
