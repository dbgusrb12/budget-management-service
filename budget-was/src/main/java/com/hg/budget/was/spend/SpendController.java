package com.hg.budget.was.spend;

import com.hg.budget.application.category.dto.CategoryDto;
import com.hg.budget.application.spend.SpendCommandService;
import com.hg.budget.application.spend.dto.SpendDto;
import com.hg.budget.was.core.annotation.AccountId;
import com.hg.budget.was.core.response.OkResponse;
import com.hg.budget.was.spend.command.CreateSpendCommand;
import com.hg.budget.was.spend.command.UpdateSpendCommand;
import com.hg.budget.was.spend.response.MySpendResponse;
import com.hg.budget.was.spend.response.SpendCategory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/spend")
public class SpendController {

    private final SpendCommandService spendCommandService;

    @PostMapping
    public OkResponse<MySpendResponse> createSpend(@AccountId String accountId, @Valid @RequestBody CreateSpendCommand command) {
        final SpendDto spend = spendCommandService.createSpend(
            command.amount(),
            command.memo(),
            command.categoryId(),
            command.spentDateTime(),
            accountId
        );

        final CategoryDto category = spend.category();
        return new OkResponse<>(
            new MySpendResponse(
                spend.id(),
                new SpendCategory(category.id(), category.name()),
                spend.amount(),
                spend.memo(),
                spend.spentDateTime(),
                spend.excludeTotal()
            )
        );
    }

    @PutMapping("/{id}")
    public OkResponse<MySpendResponse> updateSpend(
        @AccountId String accountId,
        @PathVariable Long id,
        @Valid @RequestBody UpdateSpendCommand command
    ) {
        final SpendDto spend = spendCommandService.update(
            id,
            command.amount(),
            command.memo(),
            command.categoryId(),
            command.spentDateTime(),
            accountId
        );

        final CategoryDto category = spend.category();
        return new OkResponse<>(
            new MySpendResponse(
                spend.id(),
                new SpendCategory(category.id(), category.name()),
                spend.amount(),
                spend.memo(),
                spend.spentDateTime(),
                spend.excludeTotal()
            )
        );
    }

    @DeleteMapping("/{id}")
    public void deleteSpend(@AccountId String accountId, @PathVariable Long id) {
        spendCommandService.delete(id, accountId);
    }

    @PutMapping("/{id}/exclude-total")
    public void excludeTotal(@AccountId String accountId, @PathVariable Long id) {
        spendCommandService.updateExcludeTotal(id, true, accountId);
    }

    @PutMapping("/{id}/include-total")
    public void includeTotal(@AccountId String accountId, @PathVariable Long id) {
        spendCommandService.updateExcludeTotal(id, false, accountId);
    }
}
