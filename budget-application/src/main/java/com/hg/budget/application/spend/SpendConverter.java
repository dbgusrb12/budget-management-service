package com.hg.budget.application.spend;

import com.hg.budget.application.category.dto.CategoryDto;
import com.hg.budget.application.spend.dto.SpendDto;
import com.hg.budget.application.spend.dto.SpentUser;
import com.hg.budget.core.client.DateTimeHolder;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.category.Category;
import com.hg.budget.domain.spend.Spend;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpendConverter {

    private final DateTimeHolder dateTimeHolder;

    public SpendDto convertDto(Spend spend) {
        final Category spendCategory = spend.getCategory();
        final Account spentUser = spend.getSpentUser();
        return new SpendDto(
            spend.getId(),
            new CategoryDto(spendCategory.getId(), spendCategory.getName()),
            spend.getAmount(),
            spend.getMemo(),
            new SpentUser(spentUser.getId(), spentUser.getNickname()),
            dateTimeHolder.toString(spend.getSpentDateTime()),
            spend.isExcludeTotal()
        );
    }
}
