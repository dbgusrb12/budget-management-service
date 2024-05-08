package com.hg.budget.external.infrastructure.request;

public record TextParagraph(String text) {

    public static TextParagraph from(String text) {
        return new TextParagraph(text);
    }
}
