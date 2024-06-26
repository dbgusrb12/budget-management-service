package com.hg.budget.domain.budget;

import static org.assertj.core.api.Assertions.assertThat;

import com.hg.budget.core.client.DateTimeHolder;
import com.hg.budget.core.client.IdGenerator;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.category.Category;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BudgetTest {

    @Test
    @DisplayName("ofCreated 메서드로 추가 할 예산 객체를 생성 할 수 있다.")
    void ofCreatedTest() {
        // given
        final var testHelper = new BudgetTestHelper();
        final var idGenerator = testHelper.getIdGenerator();
        final var dateTimeHolder = testHelper.getDateTimeHolder();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");

        // when
        final var budget = Budget.ofCreated(idGenerator, category, 100L, account, dateTimeHolder);

        // then
        assertThat(budget.getId()).isEqualTo(1L);
        assertThat(budget.getCategory().getName()).isEqualTo("식비");
        assertThat(budget.getAmount()).isEqualTo(100L);
        assertThat(budget.getCreatedUser().getId()).isEqualTo("hg-yu");
        assertThat(budget.getCreatedUser().getNickname()).isEqualTo("hyungyu");
        assertThat(budget.getCreatedDateTime()).isEqualTo(LocalDateTime.of(2024, 7, 12, 0, 0, 0));
        assertThat(budget.getUpdatedDateTime()).isEqualTo(LocalDateTime.of(2024, 7, 12, 0, 0, 0));
    }

    @Test
    @DisplayName("of 메서드로 예산 객체를 생성 할 수 있다.")
    void ofTest() {
        // given
        final var testHelper = new BudgetTestHelper();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");

        // when
        final var budget = Budget.of(
            1L,
            category,
            100L,
            account,
            LocalDateTime.of(2024, 7, 12, 0, 0, 0),
            LocalDateTime.of(2024, 7, 12, 0, 0, 0)
        );

        // then
        assertThat(budget.getId()).isEqualTo(1L);
        assertThat(budget.getCategory().getName()).isEqualTo("식비");
        assertThat(budget.getAmount()).isEqualTo(100L);
        assertThat(budget.getCreatedUser().getId()).isEqualTo("hg-yu");
        assertThat(budget.getCreatedUser().getNickname()).isEqualTo("hyungyu");
        assertThat(budget.getCreatedDateTime()).isEqualTo(LocalDateTime.of(2024, 7, 12, 0, 0, 0));
        assertThat(budget.getUpdatedDateTime()).isEqualTo(LocalDateTime.of(2024, 7, 12, 0, 0, 0));
    }

    @Test
    @DisplayName("ofNotExist 메서드로 빈 예산 객체를 생성 할 수 있다.")
    void ofNotExistTest() {
        // given
        // when
        final var budget = Budget.ofNotExist();

        // then
        assertThat(budget.getId()).isNull();
        assertThat(budget.getCategory()).isNull();
        assertThat(budget.getAmount()).isEqualTo(0L);
        assertThat(budget.getCreatedUser()).isNull();
        assertThat(budget.getCreatedDateTime()).isNull();
        assertThat(budget.getUpdatedDateTime()).isNull();
    }

    @Test
    @DisplayName("exist 메서드는 id 가 있다면 true 를 반환한다.")
    void existTest() {
        // given
        final var testHelper = new BudgetTestHelper();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");
        final var budget = Budget.of(
            1L,
            category,
            100L,
            account,
            LocalDateTime.of(2024, 7, 12, 0, 0, 0),
            LocalDateTime.of(2024, 7, 12, 0, 0, 0)
        );

        // when
        final var exist = budget.exist();

        // then
        assertThat(exist).isTrue();
    }

    @Test
    @DisplayName("notExist 메서드는 id 가 없다면 true 를 반환한다.")
    void notExistTest() {
        // given
        final var budget = Budget.ofNotExist();

        // when
        final var notExist = budget.notExist();

        // then
        assertThat(notExist).isTrue();
    }

    @Test
    @DisplayName("updateAmount 메서드로 금액을 변경 할 수 있다.")
    void updateAmountTest() {
        // given
        final var testHelper = new BudgetTestHelper();
        final var idGenerator = testHelper.getIdGenerator();
        final var dateTimeHolder = testHelper.getDateTimeHolder();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");
        final var budget = Budget.ofCreated(idGenerator, category, 100L, account, dateTimeHolder);

        // when
        final var updateAmount = budget.updateAmount(200L, dateTimeHolder);

        // then
        assertThat(updateAmount.getAmount()).isEqualTo(200L);
        assertThat(updateAmount.getUpdatedDateTime()).isEqualTo(LocalDateTime.of(2024, 7, 12, 0, 0, 0));
    }

    static class BudgetTestHelper {

        IdGenerator getIdGenerator() {
            return () -> 1L;
        }

        DateTimeHolder getDateTimeHolder() {
            return () -> LocalDateTime.of(2024, 7, 12, 0, 0, 0);
        }

        Category createCategory(String name) {
            return Category.of(1L, name);
        }

        Account createAccount(String id, String nickname) {
            return Account.of(
                id,
                "PASSWORD",
                nickname,
                "LIVED",
                "ROLE_USER",
                LocalDateTime.of(2024, 7, 12, 0, 0, 0),
                LocalDateTime.of(2024, 7, 12, 0, 0, 0)
            );
        }
    }
}