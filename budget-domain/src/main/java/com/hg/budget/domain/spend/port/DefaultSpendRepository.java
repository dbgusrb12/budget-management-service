package com.hg.budget.domain.spend.port;

import com.hg.budget.core.dto.Page;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.category.Category;
import com.hg.budget.domain.persistence.account.AccountEntity;
import com.hg.budget.domain.persistence.account.AccountEntityRepository;
import com.hg.budget.domain.persistence.category.CategoryEntity;
import com.hg.budget.domain.persistence.spend.SpendEntity;
import com.hg.budget.domain.persistence.spend.SpendEntityRepository;
import com.hg.budget.domain.spend.Spend;
import com.hg.budget.domain.spend.port.specification.SpendSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DefaultSpendRepository implements SpendRepository {

    private final SpendEntityRepository spendEntityRepository;
    private final AccountEntityRepository accountEntityRepository;

    @Override
    public void save(Spend spend) {
        spendEntityRepository.save(toEntity(spend));
    }

    @Override
    public void delete(Spend spend) {
        spendEntityRepository.deleteById(spend.getId());
    }

    @Override
    public Spend findById(Long id) {
        return spendEntityRepository.findById(id)
            .map(this::toDomain)
            .orElse(Spend.ofNotExist());
    }

    @Override
    public Page<Spend> findAll(SpendSpecification specification) {
        final Account account = specification.account();
        final AccountEntity accountEntity = accountEntityRepository.findByAccountId(account.getId());
        final Category category = specification.category();
        final CategoryEntity categoryEntity = category == null ? null : CategoryEntity.of(category.getId(), category.getName());
        org.springframework.data.domain.Page<SpendEntity> page = spendEntityRepository.findAll(
            specification.page(),
            specification.size(),
            specification.startSpentDateTime(),
            specification.endSpentDateTime(),
            accountEntity,
            categoryEntity,
            specification.minAmount(),
            specification.maxAmount()
        );

        return Page.of(page.getContent(), page.getTotalElements())
            .map(this::toDomain);
    }

    private Spend toDomain(SpendEntity spend) {
        final CategoryEntity categoryEntity = spend.getCategory();
        final Category category = Category.of(categoryEntity.getId(), categoryEntity.getName());
        final AccountEntity accountEntity = spend.getSpentUser();
        final Account spentUser = Account.of(
            accountEntity.getAccountId(),
            accountEntity.getPassword(),
            accountEntity.getNickname(),
            accountEntity.getStatus(),
            accountEntity.getRole(),
            accountEntity.getSignUpDateTime(),
            accountEntity.getSignInDateTime()
        );
        return Spend.of(
            spend.getId(),
            category,
            spend.getAmount(),
            spend.getMemo(),
            spentUser,
            spend.getSpentDateTime(),
            spend.getExcludeTotal()
        );
    }

    private SpendEntity toEntity(Spend spend) {
        final Category category = spend.getCategory();
        final CategoryEntity categoryEntity = CategoryEntity.of(category.getId(), category.getName());

        final Account spentUser = spend.getSpentUser();
        final AccountEntity accountEntity = accountEntityRepository.findByAccountId(spentUser.getId());

        return SpendEntity.of(
            spend.getId(),
            categoryEntity,
            spend.getAmount(),
            spend.getMemo(),
            accountEntity,
            spend.getSpentDateTime(),
            spend.isExcludeTotal()
        );
    }
}
