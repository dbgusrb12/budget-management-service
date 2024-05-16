package com.hg.budget.application.spend.dto;

import com.hg.budget.application.category.dto.CategoryDto;
import java.time.DayOfWeek;
import java.util.List;

public record SpendSummaryDto(
    MonthSpentComparisonDto monthSummaryDto,
    DayOfWeekSpentComparisonDto dayOfWeekSummaryDto,
    long consumptionRateByOtherUsers
) {

    public record MonthSpentComparisonDto(
        long consumptionRate,
        List<CategoryComparisonDto> consumptionRateByCategory
    ) {

    }

    public record CategoryComparisonDto(
        CategoryDto category,
        long consumptionRate
    ) {

    }

    public record DayOfWeekSpentComparisonDto(
        DayOfWeek dayOfWeek,
        long consumptionRate
    ) {

    }
}