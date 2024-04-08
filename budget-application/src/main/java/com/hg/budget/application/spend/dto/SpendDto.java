package com.hg.budget.application.spend.dto;

import com.hg.budget.application.category.dto.CategoryDto;
import com.hg.budget.core.client.DateTimeHolder;
import com.hg.budget.domain.spend.Spend;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SpendDto {

    private final Long id;
    private final CategoryDto category;
    private final long amount;
    private final String memo;
    private final SpentUser spentUser;
    private final String spentDateTime;
    private final boolean excludeTotal;

    public static SpendDto of(Spend spend, DateTimeHolder dateTimeHolder) {
        final CategoryDto category = CategoryDto.from(spend.getCategory());
        final SpentUser spentUser = SpentUser.from(spend.getSpentUser());
        return new SpendDto(
            spend.getId(),
            category,
            spend.getAmount(),
            spend.getMemo(),
            spentUser,
            dateTimeHolder.toString(spend.getSpentDateTime()),
            spend.isExcludeTotal()
        );
    }
}
