package com.hg.budget.application.spend;

import com.hg.budget.application.core.code.ApplicationCode;
import com.hg.budget.application.core.exception.ApplicationException;
import com.hg.budget.application.spend.dto.SpendDto;
import com.hg.budget.core.client.DateTimeHolder;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.account.AccountService;
import com.hg.budget.domain.category.Category;
import com.hg.budget.domain.category.CategoryService;
import com.hg.budget.domain.spend.Spend;
import com.hg.budget.domain.spend.SpendService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SpendCommandService {

    private final SpendService spendService;
    private final CategoryService categoryService;
    private final AccountService accountService;
    private final SpendValidator spendValidator;
    private final DateTimeHolder dateTimeHolder;

    @Transactional
    public SpendDto createSpend(long amount, String memo, Long categoryId, LocalDateTime spentDateTime, String accountId) {
        final Account account = getAccount(accountId);
        final Category category = getCategory(categoryId);
        final Spend saved = spendService.createSpend(amount, memo, category, account, spentDateTime);
        return SpendDto.of(saved, dateTimeHolder);
    }

    @Transactional
    public SpendDto update(Long id, long amount, String memo, Long categoryId, LocalDateTime spentDateTime, String accountId) {
        final Account account = getAccount(accountId);
        final Category category = getCategory(categoryId);
        final Spend updated = spendService.update(id, amount, memo, category, spentDateTime);
        spendValidator.validateExist(updated);
        spendValidator.validateOwner(updated, account);
        return SpendDto.of(updated, dateTimeHolder);
    }

    @Transactional
    public void delete(Long id, String accountId) {
        final Account account = getAccount(accountId);
        final Spend deleted = spendService.delete(id);
        spendValidator.validateExist(deleted);
        spendValidator.validateOwner(deleted, account);
    }

    @Transactional
    public SpendDto updateExcludeTotal(Long id, boolean excludeTotal, String accountId) {
        final Account account = getAccount(accountId);
        final Spend updated = spendService.updateExcludeTotal(id, excludeTotal);
        spendValidator.validateExist(updated);
        spendValidator.validateOwner(updated, account);
        return SpendDto.of(updated, dateTimeHolder);
    }

    private Account getAccount(String accountId) {
        final Account account = accountService.findAccount(accountId);
        if (account.notExist()) {
            throw new ApplicationException(ApplicationCode.BAD_REQUEST, "유저가 존재하지 않습니다.");
        }
        return account;
    }

    private Category getCategory(Long categoryId) {
        final Category category = categoryService.findCategory(categoryId);
        if (category.notExist()) {
            throw new ApplicationException(ApplicationCode.BAD_REQUEST, "카테고리가 존재하지 않습니다.");
        }
        return category;
    }
}
