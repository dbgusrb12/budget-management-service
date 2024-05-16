package com.hg.budget.was.spend.response;

import com.hg.budget.application.spend.dto.SpendSummaryDto;
import com.hg.budget.application.spend.dto.SpendSummaryDto.CategoryComparisonDto;
import com.hg.budget.application.spend.dto.SpendSummaryDto.DayOfWeekSpentComparisonDto;
import com.hg.budget.application.spend.dto.SpendSummaryDto.MonthSpentComparisonDto;
import java.time.DayOfWeek;
import java.util.List;

public record SpendSummaryResponse(
    MonthSpentComparisonResponse monthSummary,
    DayOfWeekSpentComparisonResponse dayOfWeekSummary,
    long consumptionRateByOtherUsers
) {

    public static SpendSummaryResponse from(SpendSummaryDto spendSummary) {
        return new SpendSummaryResponse(
            MonthSpentComparisonResponse.from(spendSummary.monthSummaryDto()),
            DayOfWeekSpentComparisonResponse.from(spendSummary.dayOfWeekSummaryDto()),
            spendSummary.consumptionRateByOtherUsers()
        );
    }

    public record MonthSpentComparisonResponse(
        long consumptionRate,
        List<CategoryComparisonResponse> consumptionRateByCategory
    ) {

        public static MonthSpentComparisonResponse from(MonthSpentComparisonDto monthSpentComparison) {
            final List<CategoryComparisonResponse> consumptionRateByCategory = monthSpentComparison.consumptionRateByCategory().stream()
                .map(CategoryComparisonResponse::from)
                .toList();
            return new MonthSpentComparisonResponse(monthSpentComparison.consumptionRate(), consumptionRateByCategory);
        }
    }

    public record CategoryComparisonResponse(
        SpendCategory category,
        long consumptionRate
    ) {

        public static CategoryComparisonResponse from(CategoryComparisonDto categoryComparison) {
            return new CategoryComparisonResponse(SpendCategory.from(categoryComparison.category()), categoryComparison.consumptionRate());
        }
    }

    public record DayOfWeekSpentComparisonResponse(
        DayOfWeek dayOfWeek,
        long consumptionRate
    ) {

        public static DayOfWeekSpentComparisonResponse from(DayOfWeekSpentComparisonDto dayOfWeekSpentComparison) {
            return new DayOfWeekSpentComparisonResponse(dayOfWeekSpentComparison.dayOfWeek(), dayOfWeekSpentComparison.consumptionRate());
        }
    }
}
