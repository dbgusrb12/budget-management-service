package com.hg.budget.domain.budget;

import com.hg.budget.core.client.DateTimeHolder;
import com.hg.budget.core.client.IdGenerator;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.category.Category;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Budget {

    private final Long id;
    private final Category category;
    private final long amount;
    private final Account createdUser;
    private final LocalDateTime createdDateTime;
    private final LocalDateTime updatedDateTime;

    public static Budget ofCreated(IdGenerator idGenerator, Category category, long amount, Account createdUser, DateTimeHolder dateTimeHolder) {
        final LocalDateTime now = dateTimeHolder.now();
        return new Budget(idGenerator.generateRandomLong(), category, amount, createdUser, now, now);
    }

    public static Budget of(
        Long id,
        Category category,
        long amount,
        Account createdUser,
        LocalDateTime createdDateTime,
        LocalDateTime updatedDateTime
    ) {
        return new Budget(id, category, amount, createdUser, createdDateTime, updatedDateTime);
    }

    public static Budget ofNotExist() {
        return new Budget(null, null, 0, null, null, null);
    }

    public boolean exist() {
        return id != null;
    }

    public boolean notExist() {
        return !exist();
    }

    public Budget updateAmount(long amount, DateTimeHolder dateTimeHolder) {
        final LocalDateTime updatedDateTime = dateTimeHolder.now();
        return Budget.of(id, category, amount, createdUser, createdDateTime, updatedDateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Budget budget = (Budget) o;

        return Objects.equals(id, budget.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
