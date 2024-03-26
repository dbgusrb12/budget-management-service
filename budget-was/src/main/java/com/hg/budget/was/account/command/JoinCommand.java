package com.hg.budget.was.account.command;

import jakarta.validation.constraints.NotBlank;

public record JoinCommand(@NotBlank String id, @NotBlank String password, @NotBlank String nickname) {

}
