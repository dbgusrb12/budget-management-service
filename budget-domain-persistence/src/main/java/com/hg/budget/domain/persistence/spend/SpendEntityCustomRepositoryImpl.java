package com.hg.budget.domain.persistence.spend;

import static com.hg.budget.domain.persistence.account.QAccountEntity.accountEntity;
import static com.hg.budget.domain.persistence.category.QCategoryEntity.categoryEntity;
import static com.hg.budget.domain.persistence.spend.QSpendEntity.spendEntity;

import com.hg.budget.domain.persistence.account.AccountEntity;
import com.hg.budget.domain.persistence.category.CategoryEntity;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SpendEntityCustomRepositoryImpl implements SpendEntityCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<SpendEntity> findAll(
        int page,
        int size,
        LocalDateTime startSpentDateTime,
        LocalDateTime endSpentDateTime,
        AccountEntity account,
        CategoryEntity category,
        Long minAmount,
        Long maxAmount
    ) {
        final PageRequest pageable = PageRequest.of(page == 0 ? page : page - 1, size, Direction.ASC, "spentDateTime");
        List<SpendEntity> spendList = findSpendContent(startSpentDateTime, endSpentDateTime, account, category, minAmount, maxAmount, pageable);

        JPAQuery<Long> countQuery = findSpendCount(startSpentDateTime, endSpentDateTime, account, category, minAmount, maxAmount);

        return PageableExecutionUtils.getPage(spendList, pageable, countQuery::fetchOne);
    }

    private List<SpendEntity> findSpendContent(
        LocalDateTime startSpentDateTime,
        LocalDateTime endSpentDateTime,
        AccountEntity account,
        CategoryEntity category,
        Long minAmount,
        Long maxAmount,
        Pageable pageable
    ) {
        return jpaQueryFactory
            .select(spendEntity)
            .from(spendEntity)
            .join(spendEntity.category, categoryEntity)
            .join(spendEntity.spentUser, accountEntity)
            .where(
                spendEntity.spentDateTime.after(startSpentDateTime),
                spendEntity.spentDateTime.before(endSpentDateTime),
                spendEntity.spentUser.accountId.eq(account.getAccountId()),

                equalsCategoryIfPresent(category),
                goeMinAmountIfPresent(minAmount),
                loeMaxAmountIfPresent(maxAmount)
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(new OrderSpecifier<>(Order.ASC, spendEntity.spentDateTime))
            .fetch();
    }

    private JPAQuery<Long> findSpendCount(
        LocalDateTime startSpentDateTime,
        LocalDateTime endSpentDateTime,
        AccountEntity account,
        CategoryEntity category,
        Long minAmount,
        Long maxAmount
    ) {
        return jpaQueryFactory
            .select(spendEntity.count())
            .from(spendEntity)
            .join(spendEntity.category, categoryEntity)
            .join(spendEntity.spentUser, accountEntity)
            .where(
                spendEntity.spentDateTime.after(startSpentDateTime),
                spendEntity.spentDateTime.before(endSpentDateTime),
                spendEntity.spentUser.accountId.eq(account.getAccountId()),

                equalsCategoryIfPresent(category),
                goeMinAmountIfPresent(minAmount),
                loeMaxAmountIfPresent(maxAmount)
            );
    }

    private BooleanExpression equalsCategoryIfPresent(CategoryEntity category) {
        if (category == null) {
            return null;
        }
        return spendEntity.category.id.eq(category.getId());
    }

    private BooleanExpression goeMinAmountIfPresent(Long minAmount) {
        if (minAmount == null) {
            return null;
        }
        return spendEntity.amount.goe(minAmount);
    }

    private BooleanExpression loeMaxAmountIfPresent(Long maxAmount) {
        if (maxAmount == null) {
            return null;
        }
        return spendEntity.amount.loe(maxAmount);
    }

}
