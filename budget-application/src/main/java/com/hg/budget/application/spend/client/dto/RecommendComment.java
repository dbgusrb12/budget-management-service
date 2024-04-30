package com.hg.budget.application.spend.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RecommendComment {
    EXCELLENT("절약을 잘 실천하고 있어요."),
    BAD("평균보다 더 지출중이에요."),
    DANGEROUS("예산을 초과해서 지출중이에요.");

    private final String comment;
}
