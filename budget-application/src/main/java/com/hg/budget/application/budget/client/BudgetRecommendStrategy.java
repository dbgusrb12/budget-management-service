package com.hg.budget.application.budget.client;

import com.hg.budget.application.budget.client.dto.RecommendBudget;
import com.hg.budget.domain.budget.Budget;
import java.util.List;

/*
 * 예산 추천 전략은 언제든지 변경 될 수 있다고 판단.
 * 추천하는 전략에 대한 추상화를 통해 확장성을 제공한다.
 */
public interface BudgetRecommendStrategy {

    List<RecommendBudget> recommend(long totalAmount, List<Budget> budgets);
}
