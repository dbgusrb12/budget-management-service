package com.hg.budget.domain.spend;

import static org.assertj.core.api.Assertions.assertThat;

import com.hg.budget.core.client.IdGenerator;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.category.Category;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SpendTest {

    @Test
    @DisplayName("ofCreated 메서드로 추가 할 지출 객체를 생성 할 수 있다.")
    void ofCreatedTest() {
        // given
        final var testHelper = new SpendTestHelper();
        final var idGenerator = testHelper.getIdGenerator();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");

        // when
        final var spend = Spend.ofCreated(
            idGenerator,
            category,
            1000L,
            "메모",
            account,
            LocalDateTime.of(2024, 7, 12, 0, 0, 0)
        );

        // then
        assertThat(spend.getId()).isEqualTo(1L);
        assertThat(spend.getCategory().getName()).isEqualTo("식비");
        assertThat(spend.getAmount()).isEqualTo(1000L);
        assertThat(spend.getMemo()).isEqualTo("메모");
        assertThat(spend.getSpentUser().getId()).isEqualTo("hg-yu");
        assertThat(spend.getSpentUser().getNickname()).isEqualTo("hyungyu");
        assertThat(spend.getSpentDateTime()).isEqualTo(LocalDateTime.of(2024, 7, 12, 0, 0, 0));
        assertThat(spend.isExcludeTotal()).isFalse();
    }

    @Test
    @DisplayName("of 메서드로 지출 객체를 생성 할 수 있다.")
    void ofTest() {
        // given
        final var testHelper = new SpendTestHelper();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");

        // when
        final var spend = Spend.of(
            1L,
            category,
            1000L,
            "메모",
            account,
            LocalDateTime.of(2024, 7, 12, 0, 0, 0),
            false
        );

        // then
        assertThat(spend.getId()).isEqualTo(1L);
        assertThat(spend.getCategory().getName()).isEqualTo("식비");
        assertThat(spend.getAmount()).isEqualTo(1000L);
        assertThat(spend.getMemo()).isEqualTo("메모");
        assertThat(spend.getSpentUser().getId()).isEqualTo("hg-yu");
        assertThat(spend.getSpentUser().getNickname()).isEqualTo("hyungyu");
        assertThat(spend.getSpentDateTime()).isEqualTo(LocalDateTime.of(2024, 7, 12, 0, 0, 0));
        assertThat(spend.isExcludeTotal()).isFalse();
    }

    @Test
    @DisplayName("ofNotExist 메서드로 빈 지출 객체를 생성 할 수 있다.")
    void ofNotExistTest() {
        // given
        // when
        final var spend = Spend.ofNotExist();

        // then
        assertThat(spend.getId()).isNull();
        assertThat(spend.getCategory()).isNull();
        assertThat(spend.getAmount()).isEqualTo(0);
        assertThat(spend.getMemo()).isNull();
        assertThat(spend.getSpentUser()).isNull();
        assertThat(spend.getSpentDateTime()).isNull();
        assertThat(spend.isExcludeTotal()).isFalse();
    }

    @Test
    @DisplayName("exist 메서드는 id 가 있다면 true 를 반환한다.")
    void existTest() {
        // given
        final var testHelper = new SpendTestHelper();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");
        final var spend = Spend.of(
            1L,
            category,
            1000L,
            "메모",
            account,
            LocalDateTime.of(2024, 7, 12, 0, 0, 0),
            false
        );

        // when
        final var exist = spend.exist();

        // then
        assertThat(exist).isTrue();
    }

    @Test
    @DisplayName("notExist 메서드는 id 가 없다면 true 를 반환한다.")
    void notExistTest() {
        // given
        final var spend = Spend.ofNotExist();

        // when
        final var notExist = spend.notExist();

        // then
        assertThat(notExist).isTrue();
    }

    @Test
    @DisplayName("updateExcludeTotal 메서드로 excludeTotal 값을 변경 할 수 있다.")
    void updateExcludeTotalTest() {
        // given
        final var testHelper = new SpendTestHelper();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");
        final var spend = Spend.of(
            1L,
            category,
            1000L,
            "메모",
            account,
            LocalDateTime.of(2024, 7, 12, 0, 0, 0),
            false
        );

        // when
        final var updated = spend.updateExcludeTotal(true);

        // then
        assertThat(updated.isExcludeTotal()).isTrue();
    }

    @Test
    @DisplayName("update 메서드로 지출 객체를 수정 할 수 있다.")
    void updateTest() {
        // given
        final var testHelper = new SpendTestHelper();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");
        final var spend = Spend.of(
            1L,
            category,
            1000L,
            "메모",
            account,
            LocalDateTime.of(2024, 7, 12, 0, 0, 0),
            false
        );
        final var updateSpend = new UpdateSpend(
            category,
            2000L,
            "메모변경",
            LocalDateTime.of(2025, 7, 12, 0, 0, 0)
        );

        // when
        final var updated = spend.update(updateSpend);

        // then
        assertThat(updated.getCategory().getName()).isEqualTo("식비");
        assertThat(updated.getAmount()).isEqualTo(2000L);
        assertThat(updated.getMemo()).isEqualTo("메모변경");
        assertThat(updated.getSpentDateTime()).isEqualTo(LocalDateTime.of(2025, 7, 12, 0, 0, 0));
    }

    static class SpendTestHelper {

        IdGenerator getIdGenerator() {
            return () -> 1L;
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