package com.hg.budget.was.budget.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record CreateBudgetsCommand(@NotEmpty List<@Valid CreateBudgetCommand> createBudgets) {

    public record CreateBudgetCommand(@NotNull Long categoryId, @NotNull @Positive Long amount) {

    }
}
