package com.hg.budget.domain.spend.port;

import com.hg.budget.core.dto.Page;
import com.hg.budget.domain.spend.Spend;
import com.hg.budget.domain.spend.port.specification.SpendSpecification;

public interface SpendRepository {

    void save(Spend spend);

    void delete(Spend spend);

    Spend findById(Long id);

    Page<Spend> findAll(SpendSpecification specification);
}
