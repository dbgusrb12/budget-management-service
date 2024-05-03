package com.hg.budget.application.spend;

import com.hg.budget.application.spend.client.SpendConsultingStrategy;
import com.hg.budget.application.spend.client.dto.Recommend;
import com.hg.budget.application.spend.client.dto.RecommendComment;
import com.hg.budget.application.spend.client.dto.TodaySpend;
import com.hg.budget.client.notification.client.NotificationSender;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.account.AccountService;
import com.hg.budget.domain.budget.Budget;
import com.hg.budget.domain.budget.BudgetService;
import com.hg.budget.domain.category.Category;
import com.hg.budget.domain.spend.Spend;
import com.hg.budget.domain.spend.SpendService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SpendScheduler {

    private final AccountService accountService;
    private final BudgetService budgetService;
    private final SpendService spendService;
    private final SpendConsultingStrategy spendConsultingStrategy;
    private final NotificationSender notificationSender;

    @Scheduled(cron = "0 0 8 * * *")
    public void recommendSpendSchedule() {
        accountService.findAccounts()
            .forEach(this::recommendSpend);
    }

    @Scheduled(cron = "0 0 20 * * *")
    public void todaySpendSchedule() {
        accountService.findAccounts()
            .forEach(this::todaySpend);
    }

    private void recommendSpend(Account account) {
        final List<Budget> budgets = budgetService.findBudgets(account);
        final List<Spend> spends = spendService.findSpendList(account);
        final List<Recommend> recommends = spendConsultingStrategy.recommend(budgets, spends);
        final String title = String.format("%s 님의 오늘의 지출을 추천해드려요.", account.getNickname());
        final String content = recommends.stream()
            .map(recommend -> {
                final Category category = recommend.category();
                final long amount = recommend.amount();
                final RecommendComment comment = recommend.comment();
                return String.format("카테고리 : %s,\n금액: %s,\n,평가: %s", category.getName(), amount, comment.getComment());
            }).collect(Collectors.joining("\n"));
        notificationSender.send(account.getId(), title, content);
    }

    private void todaySpend(Account account) {
        final List<Budget> budgets = budgetService.findBudgets(account);
        final List<Spend> spends = spendService.findSpendList(account);
        List<TodaySpend> todaySpends = spendConsultingStrategy.getTodaySpend(budgets, spends);
        final String title = String.format("%s 님의 오늘의 지출을 안내해드려요.", account.getNickname());
        final String content = todaySpends.stream()
            .map(todaySpend -> {
                final Category category = todaySpend.category();
                final long appropriateAmount = todaySpend.appropriateAmount();
                final long spentAmount = todaySpend.spentAmount();
                final long risk = todaySpend.risk();
                return String.format(
                    "카테고리 : %s,\n오늘 사용했으면 좋은 금액: %s,\n,실제 사용한 금액: %s\n위험도: %s",
                    category.getName(),
                    appropriateAmount,
                    spentAmount,
                    risk
                );
            }).collect(Collectors.joining("\n"));
        notificationSender.send(account.getId(), title, content);
    }
}
