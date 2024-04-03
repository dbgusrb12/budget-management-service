package com.hg.budget.was.budget.command;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RecommendCommand(@NotNull @Positive Long totalAmount) {

}
