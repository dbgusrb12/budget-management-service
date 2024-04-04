package com.hg.budget.domain.spend;

import com.hg.budget.domain.category.Category;
import java.time.LocalDateTime;

public record UpdateSpend(Category category, Long amount, String memo, LocalDateTime spentDateTime) {

}
