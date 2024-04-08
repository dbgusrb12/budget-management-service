package com.hg.budget.application.spend.dto;

import com.hg.budget.application.spend.SpendAmountCalculator;
import com.hg.budget.core.dto.Page;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SpendPage {

    private long totalAmount;
    private List<TotalAmount> totalAmountByCategories;
    private Page<SpendDto> page;

    public static SpendPage of(SpendAmountCalculator spendAmountCalculator, Page<SpendDto> spendPage) {
        return new SpendPage(
            spendAmountCalculator.getTotalAmount(),
            spendAmountCalculator.getTotalAmountByCategory(),
            spendPage
        );
    }
}
