package com.hg.budget.application.spend.scheduler;

import com.hg.budget.application.spend.client.SpendConsultingStrategy;
import com.hg.budget.application.spend.client.dto.Recommend;
import com.hg.budget.application.spend.client.dto.SpendGuide;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.account.AccountService;
import com.hg.budget.domain.budget.BudgetService;
import com.hg.budget.domain.spend.SpendService;
import com.hg.budget.external.client.NotificationSender;
import com.hg.budget.external.client.dto.Notification;
import java.util.List;
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
    private final MessageFormatter messageFormatter;

    @Scheduled(cron = "0 0 8 * * *")
    public void recommendSpendSchedule() {
        accountService.findAccounts()
            .forEach(this::recommendSpend);
    }

    @Scheduled(cron = "0 0 20 * * *")
    public void spendGuideSchedule() {
        accountService.findAccounts()
            .forEach(this::spendGuide);
    }

    private void recommendSpend(Account account) {
        final List<Recommend> recommends = spendConsultingStrategy.recommend(
            budgetService.findBudgets(account),
            spendService.findSpendList(account)
        );
        final Notification notification = messageFormatter.generateRecommendSpendMessage(account, recommends);
        notificationSender.send(notification);
    }

    private void spendGuide(Account account) {
        final List<SpendGuide> spendGuides = spendConsultingStrategy.guide(
            budgetService.findBudgets(account),
            spendService.findSpendList(account)
        );
        final Notification notification = messageFormatter.generateSpendGuideMessage(account, spendGuides);
        notificationSender.send(notification);
    }
}
