package com.hg.budget.application.spend.dto;

import com.hg.budget.core.dto.Page;

public record SpendPage(TotalAmount totalAmounts, Page<SpendDto> page) {

}
