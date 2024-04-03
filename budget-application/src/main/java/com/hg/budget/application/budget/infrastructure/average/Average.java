package com.hg.budget.application.budget.infrastructure.average;

import com.hg.budget.domain.category.Category;

public record Average(Category category, double percent) {

    public Average plus(double percent) {
        return new Average(category, this.percent + percent);
    }
}
