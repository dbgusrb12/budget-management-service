package com.hg.budget.was.spend.command;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public record CreateSpendCommand(
    @NotNull @Positive Long amount,
    String memo,
    @NotNull Long categoryId,
    @NotNull LocalDateTime spentDateTime
) {

}
