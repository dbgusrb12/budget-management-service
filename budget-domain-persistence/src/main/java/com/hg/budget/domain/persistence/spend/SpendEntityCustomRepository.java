package com.hg.budget.domain.persistence.spend;

import com.hg.budget.domain.persistence.account.AccountEntity;
import com.hg.budget.domain.persistence.category.CategoryEntity;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;

public interface SpendEntityCustomRepository {

    Page<SpendEntity> findAll(
        int page,
        int size,
        LocalDateTime startSpentDateTime,
        LocalDateTime endSpentDateTime,
        AccountEntity account,
        CategoryEntity category,
        Long minAmount,
        Long maxAmount
    );
}
