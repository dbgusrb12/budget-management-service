package com.hg.budget.application.spend.scheduler;

import com.hg.budget.application.spend.SpendConsultingService;
import com.hg.budget.application.spend.dto.RecommendDto;
import com.hg.budget.application.spend.dto.SpendGuideDto;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.account.AccountService;
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
    private final SpendConsultingService spendConsultingService;
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
        final List<RecommendDto> recommends = spendConsultingService.recommendSpend(account.getId());
        final Notification notification = messageFormatter.generateRecommendSpendMessage(account, recommends);
        notificationSender.send(notification);
    }

    private void spendGuide(Account account) {
        final List<SpendGuideDto> spendGuides = spendConsultingService.getSpendGuide(account.getId());
        final Notification notification = messageFormatter.generateSpendGuideMessage(account, spendGuides);
        notificationSender.send(notification);
    }
}
