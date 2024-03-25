package com.hg.budget.core.util;

import java.util.Random;
import java.util.UUID;

public class IdGenerator {

    private static final Random RANDOM = new Random();

    public static String generateUUID() {
        return UUID.randomUUID().toString().toUpperCase();
    }

    public static Long generateRandomLong() {
        return RANDOM.nextLong();
    }

}
