package com.hg.budget.application.spend;

import com.hg.budget.application.category.dto.CategoryDto;
import com.hg.budget.application.spend.dto.TotalAmount;
import com.hg.budget.application.spend.dto.TotalAmount.TotalAmountByCategory;
import com.hg.budget.domain.category.Category;
import com.hg.budget.domain.spend.Spend;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SpendTotalAmountCalculator {

    private final List<Spend> spendList = new ArrayList<>();

    public SpendTotalAmountCalculator(List<Spend> spendList) {
        if (spendList == null || spendList.isEmpty()) {
            return;
        }
        List<Spend> includeTotalSpendList = spendList.stream()
            .filter(Predicate.not(Spend::isExcludeTotal))
            .toList();
        this.spendList.addAll(includeTotalSpendList);
    }

    public TotalAmount calculate() {
        final List<TotalAmountByCategory> totalAmountByCategories = getTotalAmountByCategories();
        final long totalAmount = getTotalAmount(totalAmountByCategories);

        return new TotalAmount(totalAmount, totalAmountByCategories);
    }

    private long getTotalAmount(List<TotalAmountByCategory> totalAmountByCategories) {
        return totalAmountByCategories.stream()
            .mapToLong(TotalAmountByCategory::totalAmount)
            .sum();
    }

    private List<TotalAmountByCategory> getTotalAmountByCategories() {
        Map<Category, Long> totalAmountByCategory = this.spendList.stream()
            .collect(Collectors.groupingBy(
                Spend::getCategory,
                Collectors.mapping(Spend::getAmount, Collectors.summingLong(Long::longValue))
            ));
        return totalAmountByCategory.entrySet().stream()
            .map(totalAmount -> {
                final Category category = totalAmount.getKey();
                return new TotalAmountByCategory(new CategoryDto(category.getId(), category.getName()), totalAmount.getValue());
            }).toList();
    }
}
