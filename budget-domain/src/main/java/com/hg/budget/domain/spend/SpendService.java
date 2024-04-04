package com.hg.budget.domain.spend;

import com.hg.budget.core.client.IdGenerator;
import com.hg.budget.core.dto.Page;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.category.Category;
import com.hg.budget.domain.category.port.specification.SpendSpecification;
import com.hg.budget.domain.spend.port.SpendRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SpendService {

    private final IdGenerator idGenerator;
    private final SpendRepository spendRepository;

    @Transactional
    public Spend createSpend(long amount, String memo, Category category, Account account, LocalDateTime spentDateTime) {
        final Spend spend = Spend.ofCreated(idGenerator, category, amount, memo, account, spentDateTime);
        spendRepository.save(spend);
        return spend;
    }

    @Transactional
    public Spend updateExcludeTotal(Long id, boolean excludeTotal) {
        final Spend spend = findSpend(id);
        if (spend.notExist()) {
            return spend;
        }
        final Spend updateExcludeTotal = spend.updateExcludeTotal(excludeTotal);
        spendRepository.save(updateExcludeTotal);
        return updateExcludeTotal;
    }

    @Transactional
    public Spend update(Long id, long amount, String memo, Category category, LocalDateTime spentDateTime) {
        final Spend spend = findSpend(id);
        if (spend.notExist()) {
            return spend;
        }
        final UpdateSpend updateSpend = new UpdateSpend(category, amount, memo, spentDateTime);
        final Spend updated = spend.update(updateSpend);
        spendRepository.save(updated);
        return updated;
    }

    public Spend findSpend(Long id) {
        return spendRepository.findById(id);
    }

    public Page<Spend> findSpendList(
        long page,
        long size,
        LocalDateTime startSpentDateTime,
        LocalDateTime endSpentDateTime,
        Account account,
        Category category,
        Long minAmount,
        Long maxAmount
    ) {
        final SpendSpecification specification = new SpendSpecification(
            page,
            size,
            startSpentDateTime,
            endSpentDateTime,
            account,
            category,
            minAmount,
            maxAmount
        );
        return spendRepository.findAll(specification);
    }


}
