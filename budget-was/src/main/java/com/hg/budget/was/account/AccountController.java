package com.hg.budget.was.account;

import com.hg.budget.application.account.service.AccountCommandService;
import com.hg.budget.was.account.command.JoinCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountCommandService accountCommandService;

    @PostMapping("/join")
    public void signUp(@Valid @RequestBody JoinCommand command) {
        accountCommandService.createAccount(
            command.getId(),
            command.getPassword(),
            command.getNickname()
        );
    }
}
