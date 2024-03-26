package com.hg.budget.was.account.command;

import jakarta.validation.constraints.NotBlank;

public record LoginCommand(@NotBlank String id, @NotBlank String password) {

}
