package com.hg.budget.domain.mock;

import com.hg.budget.core.client.DateTimeHolder;
import java.time.LocalDateTime;

public class MockDateTimeHolder implements DateTimeHolder {

    private final LocalDateTime localDateTime;

    public MockDateTimeHolder(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public LocalDateTime now() {
        return localDateTime;
    }
}
