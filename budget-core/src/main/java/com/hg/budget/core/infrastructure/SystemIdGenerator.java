package com.hg.budget.core.infrastructure;

import com.hg.budget.core.client.IdGenerator;
import java.security.SecureRandom;
import java.util.Random;

public class SystemIdGenerator implements IdGenerator {

    private static final Random RANDOM = new SecureRandom();
    private static final long UPPER_LIMIT = (long) Math.pow(10, 16);

    @Override
    public Long generateRandomLong() {
        final long id = RANDOM.nextLong(UPPER_LIMIT);
        if (id > 0) {
            return id;
        }
        return id * -1;
    }
}
