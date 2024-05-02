package com.hg.budget.application.spend.dto;

import com.hg.budget.core.dto.Page;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SpendPage {

    private List<AmountDto> totalAmountByCategories;
    private Page<SpendDto> page;

    public static SpendPage of(List<AmountDto> totalAmountByCategories, Page<SpendDto> spendPage) {
        return new SpendPage(
            totalAmountByCategories,
            spendPage
        );
    }
}
