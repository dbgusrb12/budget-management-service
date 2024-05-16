package com.hg.budget.external.infrastructure.request;

import java.util.ArrayList;
import java.util.List;

public record Card(Header header, List<Section> sections) {

    public static Card of(String title, String subtitle) {
        final Header header = Header.of(title, subtitle);
        return new Card(header, new ArrayList<>());
    }

    public void addSection(String header, String content) {
        final Section section = Section.of(header, content);
        this.sections.add(section);
    }
}