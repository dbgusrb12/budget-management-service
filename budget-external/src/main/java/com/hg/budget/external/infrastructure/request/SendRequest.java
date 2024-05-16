package com.hg.budget.external.infrastructure.request;

import java.util.List;
import java.util.UUID;

public record SendRequest(String text, List<CardV2> cardsV2) {

    public static SendRequest ofText(String text) {
        return new SendRequest(text, null);
    }

    public static SendRequest ofCard(Card card) {
        CardV2 cardV2 = new CardV2(UUID.randomUUID().toString(), card);
        return new SendRequest(null, List.of(cardV2));
    }

    record CardV2(String cardId, Card card) {

    }
}
