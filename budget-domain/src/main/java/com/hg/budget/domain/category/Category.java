package com.hg.budget.domain.category;

import com.hg.budget.core.client.IdGenerator;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Category category = (Category) o;

        if (!Objects.equals(id, category.id)) {
            return false;
        }
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
