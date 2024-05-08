package com.hg.budget.application.spend.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {
    RECOMMEND_SPEND("오늘의 지출 추천", "%s 님의 오늘의 지출을 추천해드려요.", "- <b>금액<b/>: %,d원<br/>- <b>평가<b/>: %s"),
    TODAY_SPEND("오늘의 지출 안내", "%s 님의 오늘의 지출을 안내해드려요.", "- <b>오늘 사용했으면 좋은 금액<b/>: %,d원<br/>- <b>실제 사용한 금액<b/>: %,d원<br/>- <b>위험도<b/>: %s%%");

    private final String title;
    private final String subtitle;
    private final String content;

    public String generateSubtitle(String nickname) {
        return String.format(subtitle, nickname);
    }

    public String generateContent(Object... args) {
        return String.format(content, args);
    }
}
