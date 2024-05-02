package com.hg.budget.application.spend.infrastructure;

import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.category.Category;
import com.hg.budget.domain.spend.Spend;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CategoryFilter {

    private final LocalDate today;
    private final Map<Category, Budget> budgetByCategory;
    private final Map<Category, List<Spend>> spendsByCategory;

    public CategoryFilter(LocalDate today, List<Budget> budgets, List<Spend> spends) {
        this.today = today;
        this.budgetByCategory = budgets.stream()
            .collect(Collectors.toMap(Budget::getCategory, Function.identity()));
        this.spendsByCategory = spends.stream()
            .filter(this::isSameMonth)
            .collect(Collectors.groupingBy(
                Spend::getCategory,
                Collectors.mapping(Function.identity(), Collectors.toList())
            ));
    }

    public List<AverageSpendRecommend> filter() {
        return budgetByCategory.entrySet().stream()
            .map(categoryBudgetEntry -> {
                final List<Spend> spends = this.spendsByCategory.getOrDefault(categoryBudgetEntry.getKey(), new ArrayList<>());
                return new AverageSpendRecommend(categoryBudgetEntry.getValue(), spends);
            }).toList();
    }

    private boolean isSameMonth(Spend spend) {
        final int year = today.getYear();
        final Month month = today.getMonth();
        if (year != spend.getSpentDateTime().getYear()) {
            return false;
        }
        return month.equals(spend.getSpentDateTime().getMonth());
    }
}
