package com.hg.budget.domain.persistence.account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountEntityRepository extends JpaRepository<AccountEntity, String> {

    AccountEntity findByAccountId(String accountId);
}
