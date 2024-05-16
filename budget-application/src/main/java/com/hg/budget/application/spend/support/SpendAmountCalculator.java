package com.hg.budget.application.spend.support;

import com.hg.budget.application.spend.dto.AmountDto;
import com.hg.budget.domain.category.Category;
import com.hg.budget.domain.spend.Spend;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SpendAmountCalculator {

    private final List<Spend> spendList = new ArrayList<>();

    public SpendAmountCalculator(List<Spend> spendList) {
        if (spendList == null || spendList.isEmpty()) {
            return;
        }
        final List<Spend> includeTotalSpendList = spendList.stream()
            .filter(Predicate.not(Spend::isExcludeTotal))
            .toList();
        this.spendList.addAll(includeTotalSpendList);
    }

    public List<AmountDto> getTotalAmountByCategory() {
        final Map<Category, Long> totalAmountByCategory = this.spendList.stream()
            .collect(Collectors.groupingBy(
                Spend::getCategory,
                Collectors.mapping(Spend::getAmount, Collectors.summingLong(Long::longValue))
            ));
        return totalAmountByCategory.entrySet().stream()
            .map(totalAmount -> AmountDto.of(totalAmount.getKey(), totalAmount.getValue()))
            .sorted(Comparator.comparing(amountDto -> amountDto.category().getId()))
            .toList();
    }
}
