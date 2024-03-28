package com.hg.budget.domain.mock;

import com.hg.budget.core.config.IdGenerator;

public class MockIdGenerator implements IdGenerator {

    private Long id;

    public MockIdGenerator(Long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long generateRandomLong() {
        return id;
    }
}
