package com.hg.budget.application.spend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import com.hg.budget.application.core.code.ApplicationCode;
import com.hg.budget.application.core.exception.ApplicationException;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.persistence.account.AccountEntity;
import com.hg.budget.domain.persistence.account.AccountEntityRepository;
import com.hg.budget.domain.persistence.category.CategoryEntity;
import com.hg.budget.domain.persistence.category.CategoryEntityRepository;
import com.hg.budget.domain.persistence.spend.SpendEntityRepository;
import com.hg.budget.domain.spend.SpendService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SpendCommandServiceTest {

    @Autowired
    SpendCommandService spendCommandService;
    SpendCommandServiceTestHelper testHelper;

    @Autowired
    SpendEntityRepository spendEntityRepository;
    @Autowired
    AccountEntityRepository accountEntityRepository;
    @Autowired
    CategoryEntityRepository categoryEntityRepository;
    @Autowired
    SpendService spendService;

    @BeforeEach
    void setUp() {
        spendEntityRepository.deleteAllInBatch();
        accountEntityRepository.deleteAllInBatch();
        categoryEntityRepository.deleteAllInBatch();
        testHelper = new SpendCommandServiceTestHelper(accountEntityRepository, categoryEntityRepository);
    }

    @Test
    @DisplayName("createSpend 로 지출을 등록 할 수 있다.")
    void createSpendTest() {
        // given
        testHelper.createAccount("hg-yu", "hyungyu");
        testHelper.createCategory("식비");

        // when
        final var spend = spendCommandService.createSpend(
            1000L,
            "메모",
            1L,
            LocalDateTime.of(2024, 7, 12, 0, 0, 0),
            "hg-yu"
        );

        // then
        assertThat(spend.getAmount()).isEqualTo(1000L);
        assertThat(spend.getMemo()).isEqualTo("메모");
        assertThat(spend.getCategory().getName()).isEqualTo("식비");
        assertThat(spend.getSpentDateTime()).isEqualTo("2024-07-12T00:00");
        assertThat(spend.getSpentUser().getNickname()).isEqualTo("hyungyu");
        assertThat(spend.isExcludeTotal()).isFalse();
    }

    @Test
    @DisplayName("지출을 등록 할 때 회원이 없다면 에러가 발생한다.")
    void createSpendTest_notExistAccount() {
        // given
        testHelper.createCategory("식비");

        // when
        final var applicationException = catchThrowableOfType(
            () -> spendCommandService.createSpend(1000L, "메모", 1L, LocalDateTime.of(2024, 7, 12, 0, 0, 0), "hg-yu"),
            ApplicationException.class
        );

        // then
        assertThat(applicationException.getApplicationCode()).isEqualTo(ApplicationCode.BAD_REQUEST);
        assertThat(applicationException.getLogMessage()).isEqualTo("유저가 존재하지 않습니다.");
        assertThat(applicationException.getMessage()).isEqualTo("잘못된 요청입니다.");
    }

    @Test
    @DisplayName("지출을 등록 할 때 회원이 없다면 에러가 발생한다.")
    void createSpendTest_notExistCategory() {
        // given
        testHelper.createAccount("hg-yu", "hyungyu");

        // when
        final var applicationException = catchThrowableOfType(
            () -> spendCommandService.createSpend(1000L, "메모", 1L, LocalDateTime.of(2024, 7, 12, 0, 0, 0), "hg-yu"),
            ApplicationException.class
        );

        // then
        assertThat(applicationException.getApplicationCode()).isEqualTo(ApplicationCode.BAD_REQUEST);
        assertThat(applicationException.getLogMessage()).isEqualTo("카테고리가 존재하지 않습니다.");
        assertThat(applicationException.getMessage()).isEqualTo("잘못된 요청입니다.");
    }

    @Test
    @DisplayName("update 로 지출을 수정 할 수 있다.")
    void updateTest() {
        // given
        testHelper.createAccount("hg-yu", "hyungyu");
        testHelper.createCategory("식비");
        final var saved = spendCommandService.createSpend(
            1000L,
            "메모",
            1L,
            LocalDateTime.of(2024, 7, 12, 0, 0, 0),
            "hg-yu"
        );

        // when
        final var updated = spendCommandService.update(
            saved.getId(),
            2000L,
            "수정된메모",
            1L,
            LocalDateTime.of(2025, 7, 12, 0, 0, 0),
            "hg-yu"
        );

        // then
        assertThat(updated.getAmount()).isEqualTo(2000L);
        assertThat(updated.getMemo()).isEqualTo("수정된메모");
        assertThat(updated.getCategory().getName()).isEqualTo("식비");
        assertThat(updated.getSpentDateTime()).isEqualTo("2025-07-12T00:00");
        assertThat(updated.getSpentUser().getNickname()).isEqualTo("hyungyu");
        assertThat(updated.isExcludeTotal()).isFalse();
    }

    @Test
    @DisplayName("delete 로 지출을 삭제 할 수 있다.")
    void deleteTest() {
        // given
        final var account = testHelper.createAccount("hg-yu", "hyungyu");
        testHelper.createCategory("식비");
        final var saved = spendCommandService.createSpend(
            1000L,
            "메모",
            1L,
            LocalDateTime.of(2024, 7, 12, 0, 0, 0),
            "hg-yu"
        );

        // when
        spendCommandService.delete(saved.getId(), "hg-yu");

        // then
        final var spendList = spendService.findSpendList(account);
        assertThat(spendList.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("updateExcludeTotal 로 지출의 합계 제외 여부를 수정 할 수 있다.")
    void updateExcludeTotalTest() {
        // given
        testHelper.createAccount("hg-yu", "hyungyu");
        testHelper.createCategory("식비");
        final var saved = spendCommandService.createSpend(
            1000L,
            "메모",
            1L,
            LocalDateTime.of(2024, 7, 12, 0, 0, 0),
            "hg-yu"
        );

        // when
        final var updated = spendCommandService.updateExcludeTotal(saved.getId(), true, "hg-yu");

        // then
        assertThat(updated.isExcludeTotal()).isTrue();
    }

    static class SpendCommandServiceTestHelper {

        private final AccountEntityRepository accountEntityRepository;
        private final CategoryEntityRepository categoryEntityRepository;
        private long categoryGeneratedId = 1L;
        private long accountGeneratedId = 1L;

        SpendCommandServiceTestHelper(AccountEntityRepository accountEntityRepository, CategoryEntityRepository categoryEntityRepository) {
            this.accountEntityRepository = accountEntityRepository;
            this.categoryEntityRepository = categoryEntityRepository;
        }

        void createCategory(String name) {
            final var entity = CategoryEntity.of(categoryGeneratedId++, name);
            categoryEntityRepository.save(entity);
        }

        Account createAccount(String id, String nickname) {
            final var entity = AccountEntity.ofUpdate(
                accountGeneratedId++,
                id,
                "PASSWORD",
                nickname,
                "LIVED",
                "ROLE_USER",
                LocalDateTime.of(2024, 7, 12, 0, 0, 0),
                LocalDateTime.of(2024, 7, 12, 0, 0, 0)
            );
            accountEntityRepository.save(entity);
            return Account.of(
                entity.getAccountId(),
                entity.getPassword(),
                entity.getNickname(),
                entity.getStatus(),
                entity.getRole(),
                entity.getSignUpDateTime(),
                entity.getSignUpDateTime()
            );
        }
    }
}