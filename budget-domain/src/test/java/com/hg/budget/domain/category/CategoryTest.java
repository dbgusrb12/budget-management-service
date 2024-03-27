package com.hg.budget.domain.category;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    @DisplayName("ofCreated 메서드로 추가 할 카테고리 객체를 생성 할 수 있다.")
    void ofCreatedTest() {
        // given
        // when
        final var category = Category.ofCreated("식비");

        // then
        assertThat(category.getId()).isNotNull();
        assertThat(category.getName()).isEqualTo("식비");
    }

    @Test
    @DisplayName("of 메서드로 카테고리 객체를 생성 할 수 있다.")
    void ofTest() {
        // given
        // when
        final var category = Category.of(1L, "식비");

        // then
        assertThat(category.getId()).isEqualTo(1L);
        assertThat(category.getName()).isEqualTo("식비");
    }

    @Test
    @DisplayName("ofNotExist 메서드로 빈 카테고리 객체를 생성 할 수 있다.")
    void ofNotExistTest() {
        // given
        // when
        final var category = Category.ofNotExist();

        // then
        assertThat(category.getId()).isNull();
        assertThat(category.getName()).isNull();
    }

    @Test
    @DisplayName("exist 메서드는 id 가 있다면 true를 반환한다.")
    void existTest() {
        // given
        final var category = Category.of(1L, "식비");

        // when
        final var exist = category.exist();

        // then
        assertThat(exist).isTrue();
    }
}