package com.hg.budget.domain.budget.port;

import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.category.Category;
import com.hg.budget.domain.persistence.account.AccountEntity;
import com.hg.budget.domain.persistence.account.AccountEntityRepository;
import com.hg.budget.domain.persistence.budget.BudgetEntity;
import com.hg.budget.domain.persistence.budget.BudgetEntityRepository;
import com.hg.budget.domain.persistence.category.CategoryEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DefaultBudgetRepository implements BudgetRepository {

    private final BudgetEntityRepository budgetEntityRepository;
    private final AccountEntityRepository accountEntityRepository;

    @Override
    public void save(Budget budget) {
        budgetEntityRepository.save(toEntity(budget));
    }

    @Override
    public void delete(Budget budget) {
        budgetEntityRepository.deleteById(budget.getId());
    }

    @Override
    public Budget findById(Long id) {
        return budgetEntityRepository.findById(id)
            .map(this::toDomain)
            .orElse(Budget.ofNotExist());
    }

    @Override
    public Budget findByCategoryAndCreatedUser(Category category, Account account) {
        final BudgetEntity budgetEntity = budgetEntityRepository.findByCategory_IdAndCreatedUser_AccountId(category.getId(), account.getId());
        if (budgetEntity == null) {
            return Budget.ofNotExist();
        }
        return toDomain(budgetEntity);
    }

    @Override
    public List<Budget> findAll() {
        return budgetEntityRepository.findAll().stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Budget> findByCreatedUser(Account account) {
        return budgetEntityRepository.findByCreatedUser_AccountId(account.getId()).stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    private Budget toDomain(BudgetEntity budget) {
        final CategoryEntity categoryEntity = budget.getCategory();
        final Category category = Category.of(categoryEntity.getId(), categoryEntity.getName());

        final AccountEntity accountEntity = budget.getCreatedUser();
        final Account createdUser = Account.of(
            accountEntity.getAccountId(),
            accountEntity.getPassword(),
            accountEntity.getNickname(),
            accountEntity.getStatus(),
            accountEntity.getRole(),
            accountEntity.getSignUpDateTime(),
            accountEntity.getSignInDateTime()
        );
        return Budget.of(
            budget.getId(),
            category,
            budget.getAmount(),
            createdUser,
            budget.getCreatedDateTime(),
            budget.getUpdatedDateTime()
        );
    }

    private BudgetEntity toEntity(Budget budget) {
        final Category category = budget.getCategory();
        final CategoryEntity categoryEntity = CategoryEntity.of(category.getId(), category.getName());

        final Account createdUser = budget.getCreatedUser();
        final AccountEntity accountEntity = accountEntityRepository.findByAccountId(createdUser.getId());

        return BudgetEntity.of(
            budget.getId(),
            categoryEntity,
            budget.getAmount(),
            accountEntity,
            budget.getCreatedDateTime(),
            budget.getUpdatedDateTime()
        );
    }
}
