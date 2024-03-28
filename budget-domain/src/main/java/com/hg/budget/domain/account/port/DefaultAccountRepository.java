package com.hg.budget.domain.account.port;

import com.hg.budget.core.client.IdGenerator;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.persistence.account.AccountEntity;
import com.hg.budget.domain.persistence.account.AccountEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DefaultAccountRepository implements AccountRepository {

    private final IdGenerator idGenerator;
    private final AccountEntityRepository accountEntityRepository;

    @Override
    public void save(Account account) {
        final AccountEntity saved = AccountEntity.of(
            idGenerator,
            account.getId(),
            account.getPassword(),
            account.getNickname(),
            account.getStatus().name(),
            account.getRole().getValue(),
            account.getSignUpDateTime(),
            account.getSignInDateTime()
        );
        accountEntityRepository.save(saved);
    }

    @Override
    public Account findById(String id) {
        final AccountEntity accountEntity = accountEntityRepository.findByAccountId(id);
        if (accountEntity == null) {
            return Account.ofNotExist();
        }
        return Account.of(
            accountEntity.getAccountId(),
            accountEntity.getPassword(),
            accountEntity.getNickname(),
            accountEntity.getStatus(),
            accountEntity.getRole(),
            accountEntity.getSignUpDateTime(),
            accountEntity.getSignInDateTime()
        );
    }

    @Override
    public void update(Account account) {
        final AccountEntity accountEntity = accountEntityRepository.findByAccountId(account.getId());
        if (accountEntity == null) {
            return;
        }

        final AccountEntity updated = AccountEntity.ofUpdate(
            accountEntity.getId(),
            account.getId(),
            account.getPassword(),
            account.getNickname(),
            account.getStatus().name(),
            account.getRole().getValue(),
            account.getSignUpDateTime(),
            account.getSignInDateTime()
        );
        accountEntityRepository.save(updated);
    }
}
