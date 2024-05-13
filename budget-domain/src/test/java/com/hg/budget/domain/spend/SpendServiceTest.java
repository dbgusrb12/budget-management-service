package com.hg.budget.domain.spend;

import static org.assertj.core.api.Assertions.assertThat;

import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.category.Category;
import com.hg.budget.domain.mock.MockIdGenerator;
import com.hg.budget.domain.mock.MockSpendRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SpendServiceTest {

    SpendService spendService;
    MockIdGenerator idGenerator;

    @BeforeEach
    void setUp() {
        idGenerator = new MockIdGenerator(1L);
        spendService = new SpendService(idGenerator, new MockSpendRepository());
    }

    @Test
    @DisplayName("createSpend 로 지출을 추가 할 수 있다.")
    void createSpendTest() {
        // given
        final var testHelper = new SpendServiceTestHelper();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");

        // when
        final var spend = spendService.createSpend(
            1000L,
            "메모",
            category,
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
    @DisplayName("updateExcludeTotal 로 합계 제외 필드를 수정 할 수 있다.")
    void updateExcludeTotalTest() {
        // given
        final var testHelper = new SpendServiceTestHelper();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");
        spendService.createSpend(1000L, "메모", category, account, LocalDateTime.of(2024, 7, 12, 0, 0, 0));

        // when
        final var spend = spendService.updateExcludeTotal(1L, true);

        // then
        assertThat(spend.isExcludeTotal()).isTrue();
    }

    @Test
    @DisplayName("updateExcludeTotal 호출 시 해당 지출 정보가 없으면 빈 지출 객체가 반환된다.")
    void updateExcludeTotalTest_NotExist() {
        // given
        // when
        final var spend = spendService.updateExcludeTotal(1L, true);

        // then
        assertThat(spend.notExist()).isTrue();
    }

    @Test
    @DisplayName("update 로 지출의 정보를 수정 할 수 있다.")
    void updateTest() {
        // given
        final var testHelper = new SpendServiceTestHelper();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");
        spendService.createSpend(1000L, "메모", category, account, LocalDateTime.of(2024, 7, 12, 0, 0, 0));

        // when
        final var spend = spendService.update(
            1L,
            2000L,
            "수정된메모",
            testHelper.createCategory("여가"),
            LocalDateTime.of(2025, 7, 12, 0, 0, 0)
        );

        // then
        assertThat(spend.getAmount()).isEqualTo(2000L);
        assertThat(spend.getMemo()).isEqualTo("수정된메모");
        assertThat(spend.getCategory().getName()).isEqualTo("여가");
        assertThat(spend.getSpentDateTime()).isEqualTo(LocalDateTime.of(2025, 7, 12, 0, 0, 0));
    }

    @Test
    @DisplayName("update 호출 시 해당 지출 정보가 없으면 빈 지출 객체가 반환된다.")
    void updateTest_NotExist() {
        // given
        final var testHelper = new SpendServiceTestHelper();

        // when
        final var spend = spendService.update(
            1L,
            2000L,
            "수정된메모",
            testHelper.createCategory("식비"),
            LocalDateTime.of(2025, 7, 12, 0, 0, 0)
        );

        // then
        assertThat(spend.notExist()).isTrue();
    }

    @Test
    @DisplayName("delete 로 지출을 삭제 할 수 있다.")
    void deleteTest() {
        // given
        final var testHelper = new SpendServiceTestHelper();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");
        spendService.createSpend(1000L, "메모", category, account, LocalDateTime.of(2024, 7, 12, 0, 0, 0));

        // when
        spendService.delete(1L);
        final var spend = spendService.findSpend(1L);

        // then
        assertThat(spend.notExist()).isTrue();
    }

    @Test
    @DisplayName("delete 호출 시 해당 지출 정보가 없으면 빈 지출 객체가 반환된다.")
    void deleteTest_NotExist() {
        // given
        // when
        final var deleted = spendService.delete(1L);

        // then
        assertThat(deleted.notExist()).isTrue();
    }

    @Test
    @DisplayName("findSpend 로 지출을 조회 할 수 있다.")
    void findSpendTest() {
        // given
        final var testHelper = new SpendServiceTestHelper();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");
        spendService.createSpend(1000L, "메모", category, account, LocalDateTime.of(2024, 7, 12, 0, 0, 0));

        // when
        final var spend = spendService.findSpend(1L);

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
    @DisplayName("findSpend 호출 시 해당 지출 정보가 없으면 빈 지출 객체가 반환된다.")
    void findSpendTest_NotExist() {
        // given
        // when
        final var spend = spendService.findSpend(1L);

        // then
        assertThat(spend.notExist()).isTrue();
    }

    @Test
    @DisplayName("findSpendList 로 특정 유저의 모든 지출 내역을 조회 할 수 있다.")
    void findSpendListTest_ByAccount() {
        // given
        final var testHelper = new SpendServiceTestHelper();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");
        spendService.createSpend(1000L, "메모", category, account, LocalDateTime.of(2024, 7, 12, 0, 0, 0));
        idGenerator.setId(2L);
        spendService.createSpend(2000L, "메모2", category, account, LocalDateTime.of(2024, 7, 12, 0, 0, 0));

        // when
        final var spendList = spendService.findSpendList(account);

        // then
        assertThat(spendList.size()).isEqualTo(2);
        assertThat(spendList.get(0).getAmount()).isEqualTo(1000L);
        assertThat(spendList.get(0).getMemo()).isEqualTo("메모");
        assertThat(spendList.get(1).getAmount()).isEqualTo(2000L);
        assertThat(spendList.get(1).getMemo()).isEqualTo("메모2");
    }

    @Test
    @DisplayName("pageSpendList 로 페이징 된 지출 내역을 조회 할 수 있다.")
    void pageSpendListTest() {
        // given
        final var testHelper = new SpendServiceTestHelper();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");
        spendService.createSpend(1000L, "메모", category, account, LocalDateTime.of(2024, 7, 12, 0, 0, 0));
        idGenerator.setId(2L);
        spendService.createSpend(2000L, "메모2", category, account, LocalDateTime.of(2024, 7, 12, 0, 0, 0));

        // when
        final var spendList = spendService.pageSpendList(
            1,
            5,
            LocalDateTime.of(2024, 7, 11, 0, 0, 0),
            LocalDateTime.of(2024, 7, 13, 0, 0, 0),
            account,
            null,
            null,
            null
        );

        // then
        assertThat(spendList.getTotalElements()).isEqualTo(2);
        assertThat(spendList.getContent().get(0).getAmount()).isEqualTo(1000L);
        assertThat(spendList.getContent().get(0).getMemo()).isEqualTo("메모");
        assertThat(spendList.getContent().get(1).getAmount()).isEqualTo(2000L);
        assertThat(spendList.getContent().get(1).getMemo()).isEqualTo("메모2");
    }

    @Test
    @DisplayName("findSpendList 로 특정 날짜의 모든 유저 지출 내역을 조회 할 수 있다.")
    void findSpendListTest_ByLocalDate() {
        // given
        final var testHelper = new SpendServiceTestHelper();
        final var category = testHelper.createCategory("식비");
        final var account = testHelper.createAccount("hg-yu", "hyungyu");
        final var account2 = testHelper.createAccount("hg-yu2", "hyungyu2");
        spendService.createSpend(1000L, "메모", category, account, LocalDateTime.of(2024, 7, 12, 0, 0, 0));
        idGenerator.setId(2L);
        spendService.createSpend(2000L, "메모2", category, account2, LocalDateTime.of(2024, 7, 12, 0, 0, 0));

        // when
        final var spendList = spendService.findSpendList(LocalDate.of(2024, 7, 12));

        // then
        assertThat(spendList.size()).isEqualTo(2);
        assertThat(spendList.get(0).getAmount()).isEqualTo(1000L);
        assertThat(spendList.get(0).getMemo()).isEqualTo("메모");
        assertThat(spendList.get(1).getAmount()).isEqualTo(2000L);
        assertThat(spendList.get(1).getMemo()).isEqualTo("메모2");
    }


    static class SpendServiceTestHelper {

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