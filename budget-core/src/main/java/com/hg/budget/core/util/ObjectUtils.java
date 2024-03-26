package com.hg.budget.core.util;

import java.util.Objects;

public class ObjectUtils {

    public static String toStringOrNull(Object obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        return obj.toString();
    }
}
