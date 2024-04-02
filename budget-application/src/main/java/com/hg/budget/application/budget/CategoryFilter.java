package com.hg.budget.application.budget;

import com.hg.budget.application.core.code.ApplicationCode;
import com.hg.budget.application.core.exception.ApplicationException;
import com.hg.budget.domain.category.Category;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

class CategoryFilter {

    private final Map<Long, Category> categories;

    CategoryFilter(List<Category> categories) {
        List<Category> cloneCategory = categories;
        if (cloneCategory == null || cloneCategory.isEmpty()) {
            cloneCategory = new ArrayList<>();
        }
        this.categories = cloneCategory.stream()
            .collect(Collectors.toMap(Category::getId, Function.identity()));
    }

    Category get(Long categoryId) {
        if (notContains(categoryId)) {
            throw new ApplicationException(ApplicationCode.BAD_REQUEST, "카테고리가 존재하지 않습니다.");
        }
        return categories.get(categoryId);
    }

    private boolean notContains(Long categoryId) {
        return !categories.containsKey(categoryId);
    }
}
