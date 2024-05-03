package com.hg.budget.application.spend.client;

import com.hg.budget.application.spend.client.dto.Recommend;
import com.hg.budget.application.spend.client.dto.TodaySpend;
import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.spend.Spend;
import java.util.List;

public interface SpendConsultingStrategy {

    List<Recommend> recommend(List<Budget> budgets, List<Spend> spends);

    List<TodaySpend> getTodaySpend(List<Budget> budgets, List<Spend> spends);
}
