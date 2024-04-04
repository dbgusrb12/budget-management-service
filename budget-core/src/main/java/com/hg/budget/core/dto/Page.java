package com.hg.budget.core.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public class Page<T> {

    private final List<T> content = new ArrayList<>();
    private final long totalElements;

    private Page(List<T> content, long totalElements) {
        this.content.addAll(content);
        this.totalElements = totalElements;
    }

    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        return new Page<>(getConvertedContent(converter), this.totalElements);
    }

    private <U> List<U> getConvertedContent(Function<? super T, ? extends U> converter) {
        Assert.notNull(converter, "Function must not be null");

        return content.stream()
            .map(converter)
            .collect(Collectors.toList());
    }

    public static <T> Page<T> of(List<T> content, long totalElements) {
        Assert.notNull(content, "Content must not be null");
        return new Page<>(content, totalElements);
    }
}
