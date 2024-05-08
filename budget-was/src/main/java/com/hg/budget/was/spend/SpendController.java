package com.hg.budget.was.spend;

import com.hg.budget.application.spend.SpendCommandService;
import com.hg.budget.application.spend.SpendQueryService;
import com.hg.budget.application.spend.dto.SpendDto;
import com.hg.budget.application.spend.dto.SpendPage;
import com.hg.budget.application.spend.scheduler.SpendScheduler;
import com.hg.budget.was.core.annotation.AccountId;
import com.hg.budget.was.core.response.OkResponse;
import com.hg.budget.was.spend.command.CreateSpendCommand;
import com.hg.budget.was.spend.command.UpdateSpendCommand;
import com.hg.budget.was.spend.response.MySpendResponse;
import com.hg.budget.was.spend.response.RecommendSpendResponse;
import com.hg.budget.was.spend.response.SpendPageResponse;
import com.hg.budget.was.spend.response.TodaySpendResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/spend")
@Validated
public class SpendController {

    private final SpendCommandService spendCommandService;
    private final SpendQueryService spendQueryService;
    // FIXME 디버깅 확인용 (삭제 예정)
    private final SpendScheduler spendScheduler;

    // FIXME 디버깅 확인용 (삭제 예정)
    @GetMapping("/debug")
    public void debug() {
        spendScheduler.recommendSpendSchedule();
        spendScheduler.todaySpendSchedule();
    }

    @PostMapping
    public OkResponse<MySpendResponse> createSpend(@AccountId String accountId, @Valid @RequestBody CreateSpendCommand command) {
        final SpendDto spend = spendCommandService.createSpend(
            command.amount(),
            command.memo(),
            command.categoryId(),
            command.spentDateTime(),
            accountId
        );

        return new OkResponse<>(MySpendResponse.from(spend));
    }

    @GetMapping
    public OkResponse<SpendPageResponse> pageSpend(
        @AccountId String accountId,
        @RequestParam(defaultValue = "1") @PositiveOrZero int page,
        @RequestParam(defaultValue = "5") @Positive int size,
        @RequestParam LocalDateTime startDateTime,
        @RequestParam LocalDateTime endDateTime,
        @RequestParam(required = false) Long categoryId,
        @RequestParam(required = false) Long minAmount,
        @RequestParam(required = false) Long maxAmount
    ) {
        final SpendPage spendPage = spendQueryService.pageSpend(page, size, startDateTime, endDateTime, categoryId, minAmount, maxAmount, accountId);
        return new OkResponse<>(SpendPageResponse.from(spendPage));
    }

    @GetMapping("/{id}")
    public OkResponse<MySpendResponse> getSpend(@AccountId String accountId, @PathVariable Long id) {
        final SpendDto spend = spendQueryService.getSpend(id, accountId);
        return new OkResponse<>(MySpendResponse.from(spend));
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

        return new OkResponse<>(MySpendResponse.from(spend));
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

    @GetMapping("/recommend")
    public OkResponse<RecommendSpendResponse> recommendSpend(@AccountId String accountId) {
        final RecommendSpendResponse response = RecommendSpendResponse.from(spendQueryService.recommendSpend(accountId));
        return new OkResponse<>(response);
    }

    @GetMapping("/today")
    public OkResponse<TodaySpendResponse> getTodaySpend(@AccountId String accountId) {
        final TodaySpendResponse response = TodaySpendResponse.from(spendQueryService.getTodaySpend(accountId));
        return new OkResponse<>(response);
    }
}
