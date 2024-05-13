package com.hg.budget.domain.spend.port;

import com.hg.budget.core.dto.Page;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.spend.Spend;
import com.hg.budget.domain.spend.port.specification.SpendSpecification;
import java.time.LocalDate;
import java.util.List;

public interface SpendRepository {

    void save(Spend spend);

    void delete(Spend spend);

    Spend findById(Long id);

    List<Spend> findAll(Account account);

    Page<Spend> findAll(SpendSpecification specification);

    List<Spend> findBySpentDate(LocalDate spentDate);
}
