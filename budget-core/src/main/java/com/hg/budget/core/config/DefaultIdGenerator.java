package com.hg.budget.core.config;

import java.util.Random;

public class DefaultIdGenerator implements IdGenerator {

    private static final Random RANDOM = new Random();

    @Override
    public Long generateRandomLong() {
        return RANDOM.nextLong();
    }
}
