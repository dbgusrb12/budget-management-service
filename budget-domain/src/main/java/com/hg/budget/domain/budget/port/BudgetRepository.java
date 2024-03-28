package com.hg.budget.domain.budget.port;

import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.category.Category;
import java.util.List;

public interface BudgetRepository {

    void save(Budget budget);

    void update(Budget budget);

    void delete(Budget budget);

    Budget findById(Long id);

    Budget findByCategoryAndCreatedUser(Category category, Account account);

    List<Budget> findAll();

    List<Budget> findByCreatedUser(Account account);
}
