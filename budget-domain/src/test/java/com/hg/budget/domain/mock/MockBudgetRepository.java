package com.hg.budget.domain.mock;

import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.budget.port.BudgetRepository;
import com.hg.budget.domain.category.Category;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MockBudgetRepository implements BudgetRepository {

    private final List<Budget> budgets = new ArrayList<>();

    @Override
    public void save(Budget budget) {
        budgets.add(budget);
    }

    @Override
    public void update(Budget budget) {
        budgets.remove(budget);
        budgets.add(budget);
    }

    @Override
    public void delete(Budget budget) {
        budgets.remove(budget);
    }

    @Override
    public Budget findById(Long id) {
        return budgets.stream()
            .filter(budget -> id.equals(budget.getId()))
            .findFirst()
            .orElse(Budget.ofNotExist());
    }

    @Override
    public Budget findByCategoryAndCreatedUser(Category category, Account account) {
        return budgets.stream()
            .filter(budget -> category.equals(budget.getCategory()))
            .filter(budget -> account.equals(budget.getCreatedUser()))
            .findFirst()
            .orElse(Budget.ofNotExist());
    }

    @Override
    public List<Budget> findAll() {
        return new ArrayList<>(budgets);
    }

    @Override
    public List<Budget> findByCreatedUser(Account account) {
        return budgets.stream()
            .filter(budget -> account.equals(budget.getCreatedUser()))
            .collect(Collectors.toList());
    }
}
