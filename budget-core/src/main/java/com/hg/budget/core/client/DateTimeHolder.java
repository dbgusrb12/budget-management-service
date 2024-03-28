package com.hg.budget.core.client;

import java.time.LocalDateTime;

public interface DateTimeHolder {

    LocalDateTime now();

    default String toString(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.toString();
    }
}
