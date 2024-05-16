package com.hg.budget.application.spend.scheduler;

import com.hg.budget.application.category.dto.CategoryDto;
import com.hg.budget.application.spend.dto.RecommendDto;
import com.hg.budget.application.spend.dto.SpendGuideDto;
import com.hg.budget.application.spend.scheduler.dto.NotificationType;
import com.hg.budget.domain.account.Account;
import com.hg.budget.external.client.dto.Notification;
import com.hg.budget.external.client.dto.Notification.Content;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MessageFormatter {

    public Notification generateRecommendSpendMessage(Account account, List<RecommendDto> recommends) {
        final NotificationType type = NotificationType.RECOMMEND_SPEND;
        final String title = type.getTitle();
        final String subtitle = type.generateSubtitle(account.getNickname());
        final List<Content> contents = recommends.stream()
            .map(recommend -> {
                final CategoryDto category = recommend.category();
                final long amount = recommend.amount();
                final String comment = recommend.comment();
                final String content = type.generateContent(amount, comment);
                return new Content(category.getName(), content);
            }).toList();
        return new Notification(title, subtitle, contents);
    }

    public Notification generateSpendGuideMessage(Account account, List<SpendGuideDto> spendGuides) {
        final NotificationType type = NotificationType.GUIDE_SPEND;
        final String title = type.getTitle();
        final String subtitle = type.generateSubtitle(account.getNickname());
        final List<Content> contents = spendGuides.stream()
            .map(spendGuide -> {
                final CategoryDto category = spendGuide.category();
                final long appropriateAmount = spendGuide.appropriateAmount();
                final long spentAmount = spendGuide.spentAmount();
                final long risk = spendGuide.risk();
                final String content = type.generateContent(appropriateAmount, spentAmount, risk);
                return new Content(category.getName(), content);
            }).toList();
        return new Notification(title, subtitle, contents);
    }
}
