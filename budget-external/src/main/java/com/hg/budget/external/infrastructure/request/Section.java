package com.hg.budget.external.infrastructure.request;

import java.util.List;

public record Section(String header, boolean collapsible, int uncollapsibleWidgetsCount, List<Widget> widgets) {

    public static Section of(String header, String content) {
        return new Section(header, true, 1, List.of(Widget.from(content)));
    }
}
