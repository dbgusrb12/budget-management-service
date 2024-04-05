package com.hg.budget.domain.persistence.spend;

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

@Entity(name = "SPEND")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SpendEntity {

    @Id
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private CategoryEntity category;
    private Long amount;
    private String memo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SPENT_USER_ID")
    private AccountEntity spentUser;
    private LocalDateTime spentDateTime;
    private Boolean excludeTotal;

    public static SpendEntity of(
        Long id,
        CategoryEntity category,
        Long amount,
        String memo,
        AccountEntity spentUser,
        LocalDateTime spentDateTime,
        Boolean excludeTotal
    ) {
        return new SpendEntity(id, category, amount, memo, spentUser, spentDateTime, excludeTotal);
    }
}
