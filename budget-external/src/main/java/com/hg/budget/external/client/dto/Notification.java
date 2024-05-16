package com.hg.budget.external.client.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Notification {

    private String title;
    private String subtitle;
    private List<Content> contents;

    @Getter
    @AllArgsConstructor
    @ToString
    public static class Content {

        private String header;
        private String content;
    }
}
