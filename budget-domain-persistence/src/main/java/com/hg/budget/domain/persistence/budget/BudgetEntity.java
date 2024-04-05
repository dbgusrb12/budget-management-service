package com.hg.budget.domain.persistence.budget;

import com.hg.budget.domain.persistence.account.AccountEntity;
import com.hg.budget.domain.persistence.category.CategoryEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "BUDGET")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BudgetEntity {

    @Id
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private CategoryEntity category;
    private Long amount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CREATED_USER_ID")
    private AccountEntity createdUser;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;

    public static BudgetEntity of(
        Long id,
        CategoryEntity category,
        Long amount,
        AccountEntity createdUser,
        LocalDateTime createdDateTime,
        LocalDateTime updatedDateTime
    ) {
        return new BudgetEntity(id, category, amount, createdUser, createdDateTime, updatedDateTime);
    }
}
