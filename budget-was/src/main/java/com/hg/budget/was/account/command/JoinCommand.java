package com.hg.budget.was.account.command;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class JoinCommand {

    @NotBlank
    private String id;
    @NotBlank
    private String password;
    @NotBlank
    private String nickname;
}
