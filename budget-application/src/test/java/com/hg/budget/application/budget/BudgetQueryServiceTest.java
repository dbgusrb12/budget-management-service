package com.hg.budget.application.budget;

import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.category.Category;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class BudgetQueryServiceTest {

    @Test
    void recommendBudget() {
        Category category1 = Category.of(1L, "식비");
        Category category2 = Category.of(2L, "교통");
        Category category3 = Category.of(3L, "주거");
        Category category4 = Category.of(4L, "문화/여가");

        Account account1 = Account.of("hg-yu", "PASSWORD", "hyungyu", "LIVED", "ROLE_USER", LocalDateTime.of(2024, 7, 12, 0, 0, 0), null);
        Account account2 = Account.of("hg-yu2", "PASSWORD", "hyungyu2", "LIVED", "ROLE_USER", LocalDateTime.of(2024, 7, 12, 0, 0, 0), null);
        Account account3 = Account.of("hg-yu3", "PASSWORD", "hyungyu3", "LIVED", "ROLE_USER", LocalDateTime.of(2024, 7, 12, 0, 0, 0), null);
        List<Budget> budgets = List.of(
            Budget.of(1L, category1, 300000, account1, LocalDateTime.of(2024, 7, 12, 0, 0, 0), LocalDateTime.of(2024, 7, 12, 0, 0, 0)),
            Budget.of(2L, category2, 100000, account1, LocalDateTime.of(2024, 7, 12, 0, 0, 0), LocalDateTime.of(2024, 7, 12, 0, 0, 0)),
            Budget.of(3L, category3, 1000000, account1, LocalDateTime.of(2024, 7, 12, 0, 0, 0), LocalDateTime.of(2024, 7, 12, 0, 0, 0)),
            Budget.of(4L, category4, 50000, account1, LocalDateTime.of(2024, 7, 12, 0, 0, 0), LocalDateTime.of(2024, 7, 12, 0, 0, 0)),

            Budget.of(5L, category1, 400000, account2, LocalDateTime.of(2024, 7, 12, 0, 0, 0), LocalDateTime.of(2024, 7, 12, 0, 0, 0)),
            Budget.of(6L, category2, 100000, account2, LocalDateTime.of(2024, 7, 12, 0, 0, 0), LocalDateTime.of(2024, 7, 12, 0, 0, 0)),
            Budget.of(7L, category3, 2000000, account2, LocalDateTime.of(2024, 7, 12, 0, 0, 0), LocalDateTime.of(2024, 7, 12, 0, 0, 0)),
            Budget.of(8L, category4, 70000, account2, LocalDateTime.of(2024, 7, 12, 0, 0, 0), LocalDateTime.of(2024, 7, 12, 0, 0, 0)),

            Budget.of(9L, category1, 500000, account3, LocalDateTime.of(2024, 7, 12, 0, 0, 0), LocalDateTime.of(2024, 7, 12, 0, 0, 0)),
            Budget.of(10L, category2, 100000, account3, LocalDateTime.of(2024, 7, 12, 0, 0, 0), LocalDateTime.of(2024, 7, 12, 0, 0, 0)),
            Budget.of(11L, category3, 500000, account3, LocalDateTime.of(2024, 7, 12, 0, 0, 0), LocalDateTime.of(2024, 7, 12, 0, 0, 0)),
            Budget.of(12L, category4, 120000, account3, LocalDateTime.of(2024, 7, 12, 0, 0, 0), LocalDateTime.of(2024, 7, 12, 0, 0, 0))
        );

        AverageCalculator averageCalculator = new AverageCalculator(budgets);
        averageCalculator.calculate();
        averageCalculator.print();
//        Map<Account, List<Budget>> budgetByAccount = budgets.stream()
//            .collect(Collectors.groupingBy(Budget::getCreatedUser, Collectors.mapping(Function.identity(), Collectors.toList())));
//        Map<Category, List<Long>> percentage = new HashMap<>();
//        for (List<Budget> value : budgetByAccount.values()) {
//            long totalAmount = value.stream()
//                .mapToLong(Budget::getAmount)
//                .sum();
//            for (Budget budget : value) {
//                List<Long> percents = percentage.getOrDefault(budget.getCategory(), new ArrayList<>());
//                double percent = budget.getAmount() / (double) totalAmount * 100;
//                percents.add(Math.round(percent));
//                percentage.put(budget.getCategory(), percents);
//            }
//        }
//        percentage.forEach((key, value) -> {
//            double average = value.stream()
//                .mapToLong(v -> v)
//                .average()
//                .orElse(0);
//            System.out.println(key + ":" + Math.round(average));
//        });
    }

    static class AverageCalculator {

        private final Map<Account, Long> budgetByAccount;
        private final Map<Category, List<Long>> amountPercentByCategory = new HashMap<>();

        AverageCalculator(List<Budget> budgets) {
            List<Budget> cloneBudgets = budgets;
            if (cloneBudgets == null) {
                cloneBudgets = new ArrayList<>();
            }
            this.budgetByAccount = cloneBudgets.stream()
                .collect(
                    Collectors.groupingBy(Budget::getCreatedUser, Collectors.mapping(Budget::getAmount, Collectors.summingLong(Long::longValue))));
        }

        void print() {
            amountPercentByCategory.forEach((key, value) -> {
                double average = value.stream()
                    .mapToLong(v -> v)
                    .average()
                    .orElse(0);
                System.out.println(key + ":" + Math.round(average));
            });
        }

        void calculate() {
            budgetByAccount.values().forEach(System.out::println);
//            for (List<Budget> value : budgetByAccount.values()) {
//                long totalAmount = getTotalAmount(value);
//                for (Budget budget : value) {
//                    final long percentage = roundPercentage(totalAmount, budget.getAmount());
//                    List<Long> percents = amountPercentByCategory.computeIfAbsent(budget.getCategory(), key -> new ArrayList<>());
//                    percents.add(percentage);
//                }
//            }
        }

        private long getTotalAmount(List<Budget> budgets) {
            return budgets.stream()
                .mapToLong(Budget::getAmount)
                .sum();
        }

        private long roundPercentage(long totalAmount, long amount) {
            return Math.round(getPercentage(totalAmount, amount));
        }

        private double getPercentage(long totalAmount, long amount) {
            return amount / (double) totalAmount * 100;
        }
    }
}