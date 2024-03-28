package com.hg.budget.core.infrastructure;

import com.hg.budget.core.client.DateTimeHolder;
import java.time.LocalDateTime;

public class SystemDateTimeHolder implements DateTimeHolder {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
