package com.hg.budget.application.spend.client.dto;

import com.hg.budget.domain.category.Category;

public record TodaySpend(Category category, long appropriateAmount, long spendAmount, long risk) {

}
