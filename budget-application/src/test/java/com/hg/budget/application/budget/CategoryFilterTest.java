package com.hg.budget.application.budget;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import com.hg.budget.application.core.code.ApplicationCode;
import com.hg.budget.application.core.exception.ApplicationException;
import com.hg.budget.domain.category.Category;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CategoryFilterTest {

    @Test
    @DisplayName("category 리스트를 기반으로 CategoryFilter 객체를 생성 할 수 있다.")
    void newInstanceTest() {
        // given
        final var categories = List.of(
            Category.of(1L, "식비"),
            Category.of(2L, "교통"),
            Category.of(3L, "주거"),
            Category.of(4L, "문화/여가")
        );

        // when
        final var categoryFilter = new CategoryFilter(categories);

        // then
        assertThat(categoryFilter).isNotNull();
    }

    @Test
    @DisplayName("get 으로 특정 id 의 카테고리를 조회 할 수 있다.")
    void getCategoryTest() {
        // given
        final var categories = List.of(
            Category.of(1L, "식비"),
            Category.of(2L, "교통"),
            Category.of(3L, "주거"),
            Category.of(4L, "문화/여가")
        );
        final var categoryFilter = new CategoryFilter(categories);

        // when
        final var category = categoryFilter.get(1L);

        // then
        assertThat(category.getId()).isEqualTo(1L);
        assertThat(category.getName()).isEqualTo("식비");
    }

    @Test
    @DisplayName("특정 id 의 카테고리가 없다면 에러가 발생한다.")
    void getCategoryTest_NotExist() {
        // given
        final var categories = List.of(
            Category.of(1L, "식비"),
            Category.of(2L, "교통"),
            Category.of(3L, "주거"),
            Category.of(4L, "문화/여가")
        );
        final var categoryFilter = new CategoryFilter(categories);

        // when
        final var applicationException = catchThrowableOfType(
            () -> categoryFilter.get(6L),
            ApplicationException.class
        );

        // then
        assertThat(applicationException.getApplicationCode()).isEqualTo(ApplicationCode.BAD_REQUEST);
        assertThat(applicationException.getLogMessage()).isEqualTo("카테고리가 존재하지 않습니다.");
        assertThat(applicationException.getMessage()).isEqualTo("잘못된 요청입니다.");
    }

}