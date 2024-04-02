package com.hg.budget.domain.persistence.budget;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetEntityRepository extends JpaRepository<BudgetEntity, Long> {

    BudgetEntity findByCategory_IdAndCreatedUser_AccountId(Long id, String accountId);

    List<BudgetEntity> findByCreatedUser_AccountId(String accountId);
}
