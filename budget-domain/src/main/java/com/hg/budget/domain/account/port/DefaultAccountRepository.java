package com.hg.budget.domain.account.port;

import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.persistence.account.AccountEntity;
import com.hg.budget.domain.persistence.account.AccountEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DefaultAccountRepository implements AccountRepository {

    private final AccountEntityRepository accountEntityRepository;

    @Override
    public Account save(Account account) {
        AccountEntity saved = accountEntityRepository.save(
            AccountEntity.of(
                account.getId(),
                account.getPassword(),
                account.getNickname(),
                account.getStatus().name(),
                account.getRole().getValue(),
                account.getSignUpDateTime(),
                account.getSignInDateTime()
            ));
        return Account.of(
            saved.getAccountId(),
            saved.getPassword(),
            saved.getNickname(),
            saved.getStatus(),
            saved.getRole(),
            saved.getSignUpDateTime(),
            saved.getSignInDateTime()
        );
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
}
