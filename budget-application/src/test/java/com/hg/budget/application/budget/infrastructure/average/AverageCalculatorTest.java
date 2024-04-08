package com.hg.budget.application.budget.infrastructure.average;

import static org.assertj.core.api.Assertions.assertThat;

import com.hg.budget.domain.category.Category;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AverageCalculatorTest {

    @Test
    @DisplayName("AverageCalculator 을 사용해 퍼센트 기반으로 카테고리 별 금액을 계산 할 수 있다.")
    void calculateTest() {
        // given
        final var testHelper = new AverageCalculatorTestHelper();
        final var 식비 = testHelper.createCategory("식비");
        final var 교통 = testHelper.createCategory("교통");
        final var 주거 = testHelper.createCategory("주거");
        final var 문화여가 = testHelper.createCategory("문화/여가");
        final var 카페 = testHelper.createCategory("카페");
        final var 여행 = testHelper.createCategory("여행");
        final var 교육 = testHelper.createCategory("교육");
        final var 기타 = testHelper.createCategory("기타");
        final var averages = List.of(
            testHelper.createAverage(식비, 10),
            testHelper.createAverage(교통, 10),
            testHelper.createAverage(주거, 17),
            testHelper.createAverage(문화여가, 20),
            testHelper.createAverage(카페, 16),
            testHelper.createAverage(여행, 11),
            testHelper.createAverage(교육, 10),
            testHelper.createAverage(기타, 6)
        );
        final var averageCalculator = new AverageCalculator(200000, averages, 기타);

        // when
        final var recommends = averageCalculator.calculate();

        // then
        assertThat(recommends.get(0).amount()).isEqualTo(20000);
        assertThat(recommends.get(0).category().getName()).isEqualTo("식비");
        assertThat(recommends.get(1).amount()).isEqualTo(20000);
        assertThat(recommends.get(1).category().getName()).isEqualTo("교통");
        assertThat(recommends.get(2).amount()).isEqualTo(34000);
        assertThat(recommends.get(2).category().getName()).isEqualTo("주거");
        assertThat(recommends.get(3).amount()).isEqualTo(40000);
        assertThat(recommends.get(3).category().getName()).isEqualTo("문화/여가");
        assertThat(recommends.get(4).amount()).isEqualTo(32000);
        assertThat(recommends.get(4).category().getName()).isEqualTo("카페");
        assertThat(recommends.get(5).amount()).isEqualTo(22000);
        assertThat(recommends.get(5).category().getName()).isEqualTo("여행");
        assertThat(recommends.get(6).amount()).isEqualTo(20000);
        assertThat(recommends.get(6).category().getName()).isEqualTo("교육");
        assertThat(recommends.get(7).amount()).isEqualTo(12000);
        assertThat(recommends.get(7).category().getName()).isEqualTo("기타");

    }

    static class AverageCalculatorTestHelper {

        private long categoryGeneratedId = 1L;

        AverageCalculatorTestHelper() {
        }

        Category createCategory(String name) {
            return Category.of(categoryGeneratedId++, name);
        }

        Average createAverage(Category category, long percent) {
            return new Average(category, percent);
        }
    }
}