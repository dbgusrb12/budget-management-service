package com.hg.budget.external.infrastructure.request;

public record Widget(TextParagraph textParagraph) {

    public static Widget from(String text) {
        return new Widget(TextParagraph.from(text));
    }
}
