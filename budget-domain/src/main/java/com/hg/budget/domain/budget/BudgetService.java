package com.hg.budget.domain.budget;

import com.hg.budget.core.client.DateTimeHolder;
import com.hg.budget.core.client.IdGenerator;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.budget.port.BudgetRepository;
import com.hg.budget.domain.category.Category;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BudgetService {

    private final IdGenerator idGenerator;
    private final DateTimeHolder dateTimeHolder;
    private final BudgetRepository budgetRepository;

    @Transactional
    public Budget createBudget(long amount, Category category, Account account) {
        final Budget duplicatedBudget = findBudget(category, account);
        if (duplicatedBudget.exist()) {
            throw new IllegalArgumentException("중복된 예산 입니다.");
        }
        final Budget budget = Budget.ofCreated(idGenerator, category, amount, account, dateTimeHolder);
        budgetRepository.save(budget);
        return budget;
    }

    @Transactional
    public Budget updateAmount(Long id, long amount) {
        final Budget budget = findBudget(id);
        if (budget.notExist()) {
            return budget;
        }
        final Budget updateAmount = budget.updateAmount(amount, dateTimeHolder);
        budgetRepository.save(updateAmount);
        return updateAmount;
    }

    public Budget findBudget(Long id) {
        return budgetRepository.findById(id);
    }

    public Budget findBudget(Category category, Account account) {
        return budgetRepository.findByCategoryAndCreatedUser(category, account);
    }

    public List<Budget> findBudgets(Account account) {
        return budgetRepository.findByCreatedUser(account);
    }

    public List<Budget> findBudgets() {
        return budgetRepository.findAll();
    }
}
