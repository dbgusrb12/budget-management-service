package com.hg.budget.was.budget;

import com.hg.budget.application.budget.BudgetCommandService;
import com.hg.budget.application.budget.dto.CreateBudget;
import com.hg.budget.was.budget.command.CreateBudgetsCommand;
import com.hg.budget.was.budget.command.UpdateAmountCommand;
import com.hg.budget.was.core.annotation.AccountId;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/budgets")
public class BudgetController {

    private final BudgetCommandService budgetCommandService;

    @PostMapping
    public void createBudget(@AccountId String accountId, @Valid @RequestBody CreateBudgetsCommand command) {
        final List<CreateBudget> createBudgets = command.createBudgets().stream()
            .map(createBudgetCommand -> new CreateBudget(createBudgetCommand.categoryId(), createBudgetCommand.amount()))
            .toList();
        budgetCommandService.createBudgets(createBudgets, accountId);
    }

    @PutMapping("/{id}")
    public void updateBudget(@PathVariable Long id, @AccountId String accountId, @Valid @RequestBody UpdateAmountCommand command) {
        // TODO 예산 여러개를 동시에 수정하는 기능이 필요한지?
        budgetCommandService.updateAmount(id, command.amount(), accountId);
    }
}
