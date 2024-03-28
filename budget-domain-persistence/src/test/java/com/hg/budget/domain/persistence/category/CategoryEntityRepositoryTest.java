package com.hg.budget.domain.persistence.category;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CategoryEntityRepositoryTest {

    @Autowired
    CategoryEntityRepository categoryEntityRepository;

    @Test
    void findByNameTest() {
        // given
        final var categoryEntity = CategoryEntity.of(1L, "식비");
        categoryEntityRepository.save(categoryEntity);

        // when
        final var category = categoryEntityRepository.findByName("식비");

        // then
        assertThat(category.getId()).isEqualTo(1L);
        assertThat(category.getName()).isEqualTo("식비");
    }
}