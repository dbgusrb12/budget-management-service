package com.hg.budget.domain.spend.port;

import com.hg.budget.core.dto.Page;
import com.hg.budget.domain.spend.port.specification.SpendSpecification;
import com.hg.budget.domain.spend.Spend;

public interface SpendRepository {

    void save(Spend spend);

    Spend findById(Long id);

    Page<Spend> findAll(SpendSpecification specification);
}
