package com.hg.budget.application.budget.infrastructure.average;

import com.hg.budget.domain.category.Category;

public record Average(Category category, long percent) {

    public Average plus(long percent) {
        return new Average(category, this.percent + percent);
    }
}
