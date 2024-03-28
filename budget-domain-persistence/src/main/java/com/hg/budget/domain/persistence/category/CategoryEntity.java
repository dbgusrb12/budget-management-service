package com.hg.budget.domain.persistence.category;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "CATEGORY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryEntity {

    @Id
    private Long id;
    private String name;

    public static CategoryEntity of(Long id, String name) {
        return new CategoryEntity(id, name);
    }
}
