package com.hg.budget.domain.spending;

import com.hg.budget.core.client.IdGenerator;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.category.Category;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Spend {

    private final Long id;
    private final Category category;
    private final long amount;
    private final String memo;
    private final Account spentUser;
    private final LocalDateTime spentDateTime;
    private final boolean excludeTotal;

    public static Spend ofCreated(
        IdGenerator idGenerator,
        Category category,
        long amount,
        String memo,
        Account spentUser,
        LocalDateTime spentDateTime
    ) {
        return new Spend(idGenerator.generateRandomLong(), category, amount, memo, spentUser, spentDateTime, false);
    }

    public static Spend of(
        Long id,
        Category category,
        long amount,
        String memo,
        Account spentUser,
        LocalDateTime spentDateTime,
        boolean excludeTotal
    ) {
        return new Spend(id, category, amount, memo, spentUser, spentDateTime, excludeTotal);
    }

    public static Spend ofNotExist() {
        return new Spend(null, null, 0, null, null, null, false);
    }

    public boolean exist() {
        return id != null;
    }

    public boolean notExist() {
        return !exist();
    }

    public Spend updateExcludeTotal(boolean excludeTotal) {
        return Spend.of(id, category, amount, memo, spentUser, spentDateTime, excludeTotal);
    }

    public Spend update(UpdateSpend updateSpend) {
        return Spend.of(id, updateSpend.category(), updateSpend.amount(), updateSpend.memo(), spentUser, updateSpend.spentDateTime(), excludeTotal);
    }
}