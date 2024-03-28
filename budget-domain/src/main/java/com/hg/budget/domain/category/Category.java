package com.hg.budget.domain.category;

import com.hg.budget.core.config.IdGenerator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Category {

    private final Long id;
    private final String name;

    public static Category ofCreated(IdGenerator idGenerator, String name) {
        return new Category(idGenerator.generateRandomLong(), name);
    }

    public static Category of(Long id, String name) {
        return new Category(id, name);
    }

    public static Category ofNotExist() {
        return new Category(null, null);
    }

    public boolean exist() {
        return id != null;
    }
}
