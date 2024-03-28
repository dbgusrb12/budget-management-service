package com.hg.budget.domain.persistence.mock;

import com.hg.budget.core.config.IdGenerator;

public class MockIdGenerator implements IdGenerator {

    private final Long id;

    public MockIdGenerator(Long id) {
        this.id = id;
    }

    @Override
    public Long generateRandomLong() {
        return id;
    }
}
