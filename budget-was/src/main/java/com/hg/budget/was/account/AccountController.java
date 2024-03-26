package com.hg.budget.was.account;

import com.hg.budget.application.account.service.AccountCommandService;
import com.hg.budget.application.account.service.AccountQueryService;
import com.hg.budget.application.account.service.dto.AccountDto;
import com.hg.budget.was.account.command.JoinCommand;
import com.hg.budget.was.account.response.MyInfoResponse;
import com.hg.budget.was.core.annotation.AccountId;
import com.hg.budget.was.core.response.OkResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountCommandService accountCommandService;
    private final AccountQueryService accountQueryService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/join")
    public void signUp(@Valid @RequestBody JoinCommand command) {
        accountCommandService.createAccount(command.id(), passwordEncoder.encode(command.password()), command.nickname());
    }

    @PostMapping("/login")
    public void login(@AccountId String id) {
        accountCommandService.login(id);
    }

    @GetMapping("/me")
    public OkResponse<MyInfoResponse> getMyInfo(@AccountId String id) {
        final AccountDto account = accountQueryService.findAccount(id);
        return new OkResponse<>(
            new MyInfoResponse(
                account.getId(),
                account.getNickname(),
                account.getStatus(),
                account.getRole(),
                account.getSignUpDateTime(),
                account.getSignInDateTime()
            )
        );
    }
}
