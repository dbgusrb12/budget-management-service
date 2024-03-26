package com.hg.budget.was.account;

import com.hg.budget.application.account.service.AccountCommandService;
import com.hg.budget.was.account.command.JoinCommand;
import com.hg.budget.was.core.annotation.AccountId;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountCommandService accountCommandService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/join")
    public void signUp(@Valid @RequestBody JoinCommand command) {
        accountCommandService.createAccount(command.id(), passwordEncoder.encode(command.password()), command.nickname());
    }

    @PostMapping("/login")
    public void login(@AccountId String id) {
        accountCommandService.login(id);
    }
}
