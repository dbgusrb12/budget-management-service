package com.hg.budget.application.spend.scheduler;

import com.hg.budget.application.spend.client.dto.Recommend;
import com.hg.budget.application.spend.client.dto.RecommendComment;
import com.hg.budget.application.spend.client.dto.SpendGuide;
import com.hg.budget.application.spend.scheduler.dto.NotificationType;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.category.Category;
import com.hg.budget.external.client.dto.Notification;
import com.hg.budget.external.client.dto.Notification.Content;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MessageFormatter {

    public Notification generateRecommendSpendMessage(Account account, List<Recommend> recommends) {
        final NotificationType type = NotificationType.RECOMMEND_SPEND;
        final String title = type.getTitle();
        final String subtitle = type.generateSubtitle(account.getNickname());
        final List<Content> contents = recommends.stream()
            .map(recommend -> {
                final Category category = recommend.category();
                final long amount = recommend.amount();
                final RecommendComment comment = recommend.comment();
                final String content = type.generateContent(amount, comment.getComment());
                return new Content(category.getName(), content);
            }).toList();
        return new Notification(title, subtitle, contents);
    }

    public Notification generateSpendGuideMessage(Account account, List<SpendGuide> spendGuides) {
        final NotificationType type = NotificationType.GUIDE_SPEND;
        final String title = type.getTitle();
        final String subtitle = type.generateSubtitle(account.getNickname());
        final List<Content> contents = spendGuides.stream()
            .map(spendGuide -> {
                final Category category = spendGuide.category();
                final long appropriateAmount = spendGuide.appropriateAmount();
                final long spentAmount = spendGuide.spentAmount();
                final long risk = spendGuide.risk();
                final String content = type.generateContent(appropriateAmount, spentAmount, risk);
                return new Content(category.getName(), content);
            }).toList();
        return new Notification(title, subtitle, contents);
    }
}
