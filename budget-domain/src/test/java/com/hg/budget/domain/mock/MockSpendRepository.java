package com.hg.budget.domain.mock;

import com.hg.budget.core.dto.Page;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.spend.Spend;
import com.hg.budget.domain.spend.port.SpendRepository;
import com.hg.budget.domain.spend.port.specification.SpendSpecification;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MockSpendRepository implements SpendRepository {

    private final List<Spend> spendList = new ArrayList<>();

    @Override
    public void save(Spend spend) {
        spendList.remove(spend);
        spendList.add(spend);
    }

    @Override
    public void delete(Spend spend) {
        spendList.remove(spend);
    }

    @Override
    public Spend findById(Long id) {
        return spendList.stream()
            .filter(spend -> id.equals(spend.getId()))
            .findFirst()
            .orElse(Spend.ofNotExist());
    }

    @Override
    public List<Spend> findAll(Account account) {
        return spendList.stream()
            .filter(spend -> account.equals(spend.getSpentUser()))
            .collect(Collectors.toList());
    }

    @Override
    public Page<Spend> findAll(SpendSpecification specification) {
        final List<Spend> spends = spendList.stream()
            .filter(spend ->
                spend.getSpentDateTime().isAfter(specification.startSpentDateTime())
                    && spend.getSpentDateTime().isBefore(specification.endSpentDateTime())
            )
            .filter(spend -> spend.getSpentUser().equals(specification.account()))
            .filter(spend -> {
                if (specification.category() == null) {
                    return true;
                }
                return specification.category().equals(spend.getCategory());
            })
            .filter(spend -> {
                if (specification.minAmount() == null) {
                    return true;
                }
                return specification.minAmount() <= spend.getAmount();
            })
            .filter(spend -> {
                if (specification.maxAmount() == null) {
                    return true;
                }
                return specification.maxAmount() >= spend.getAmount();
            })
            .sorted(Comparator.comparing(Spend::getSpentDateTime))
            .toList();
        final List<Spend> page = spends.stream()
            .skip((specification.page() - 1) * specification.size())
            .limit(specification.size())
            .toList();
        return Page.of(page, spendList.size());
    }
}
