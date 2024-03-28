package com.hg.budget.core.infrastructure;

import com.hg.budget.core.client.IdGenerator;
import java.util.Random;

public class SystemIdGenerator implements IdGenerator {

    private static final Random RANDOM = new Random();

    @Override
    public Long generateRandomLong() {
        return RANDOM.nextLong();
    }
}
