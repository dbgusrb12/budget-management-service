package com.hg.budget.application.spend;

import com.hg.budget.application.core.code.ApplicationCode;
import com.hg.budget.application.core.exception.ApplicationException;
import com.hg.budget.application.spend.dto.SpendDto;
import com.hg.budget.core.dto.Page;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.account.AccountService;
import com.hg.budget.domain.category.Category;
import com.hg.budget.domain.category.CategoryService;
import com.hg.budget.domain.spend.Spend;
import com.hg.budget.domain.spend.SpendService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpendQueryService {

    private final SpendService spendService;
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final SpendValidator spendValidator;
    private final SpendConverter spendConverter;

    public SpendDto getSpend(Long id, String accountId) {
        final Account account = getAccount(accountId);

        final Spend spend = spendService.findSpend(id);
        spendValidator.validateExist(spend);
        spendValidator.validateOwner(spend, account);
        return spendConverter.convertDto(spend);
    }

    public Page<SpendDto> pageSpend(
        int page,
        int size,
        LocalDateTime startSpentDateTime,
        LocalDateTime endSpentDateTime,
        Long categoryId,
        Long minAmount,
        Long maxAmount,
        String accountId
    ) {
        final Account account = getAccount(accountId);
        final Category category = getCategory(categoryId);
        Page<Spend> spendPage = spendService.pageSpendList(page, size, startSpentDateTime, endSpentDateTime, account, category, minAmount, maxAmount);
        return spendPage.map(spendConverter::convertDto);
    }

    private Account getAccount(String accountId) {
        final Account account = accountService.findAccount(accountId);
        if (account.notExist()) {
            throw new ApplicationException(ApplicationCode.BAD_REQUEST, "유저가 존재하지 않습니다.");
        }
        return account;
    }

    private Category getCategory(Long categoryId) {
        if (categoryId == null) {
            return Category.ofNotExist();
        }
        final Category category = categoryService.findCategory(categoryId);
        if (category.notExist()) {
            throw new ApplicationException(ApplicationCode.BAD_REQUEST, "카테고리가 존재하지 않습니다.");
        }
        return category;
    }
}
