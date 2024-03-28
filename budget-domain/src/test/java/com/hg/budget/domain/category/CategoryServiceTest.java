package com.hg.budget.domain.category;

import static org.assertj.core.api.Assertions.assertThat;

import com.hg.budget.domain.category.port.CategoryRepository;
import com.hg.budget.domain.category.port.DefaultCategoryRepository;
import com.hg.budget.domain.mock.MockIdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CategoryServiceTest {

    CategoryService categoryService;
    CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository = new DefaultCategoryRepository();
        categoryService = new CategoryService(new MockIdGenerator(1L), categoryRepository);
    }

    @Test
    @DisplayName("카테고리를 생성 할 수 있다.")
    void createCategoryTest() {
        // given
        // when
        final var savedCategory = categoryService.createCategory("식비");

        // then
        assertThat(savedCategory.getId()).isEqualTo(1L);
        assertThat(savedCategory.getName()).isEqualTo("식비");
    }

    @Test
    @DisplayName("생성 할 카테고리가 이미 존재한다면 해당 객체를 반환한다.")
    void createCategoryTest_exist() {
        // given
        categoryRepository.save(Category.ofCreated(new MockIdGenerator(1L), "식비"));

        // when
        final var savedCategory = categoryService.createCategory("식비");

        // then
        assertThat(savedCategory.getId()).isEqualTo(1L);
        assertThat(savedCategory.getName()).isEqualTo("식비");
    }

    @Test
    @DisplayName("이름을 기반으로 카테고리를 조회 할 수 있다.")
    void findCategoryTest_ByName() {
        // given
        categoryRepository.save(Category.ofCreated(new MockIdGenerator(1L), "식비"));

        // when
        final var findCategory = categoryService.findCategory("식비");

        // then
        assertThat(findCategory.getId()).isEqualTo(1L);
        assertThat(findCategory.getName()).isEqualTo("식비");
    }

    @Test
    @DisplayName("해당 이름이 없다면 빈 객체를 반환한다.")
    void findCategoryTest_ByName_NotExist() {
        // given
        // when
        final var findCategory = categoryService.findCategory("식비");

        // then
        assertThat(findCategory.getId()).isNull();
        assertThat(findCategory.getName()).isNull();
    }

    @Test
    @DisplayName("아이디를 기반으로 카테고리를 조회 할 수 있다.")
    void findCategoryTest_ById() {
        // given
        categoryRepository.save(Category.ofCreated(new MockIdGenerator(1L), "식비"));

        // when
        final var findCategory = categoryService.findCategory(1L);

        // then
        assertThat(findCategory.getId()).isEqualTo(1L);
        assertThat(findCategory.getName()).isEqualTo("식비");
    }

    @Test
    @DisplayName("해당 아이디가 없다면 빈 객체를 반환한다.")
    void findCategoryTest_ById_NotExist() {
        // given
        // when
        final var findCategory = categoryService.findCategory(1L);

        // then
        assertThat(findCategory.getName()).isNull();
    }


    @Test
    @DisplayName("전체 카테고리를 조회 할 수 있다.")
    void findCategoriesTest() {
        // given
        MockIdGenerator idGenerator = new MockIdGenerator(1L);
        categoryRepository.save(Category.ofCreated(idGenerator, "식비"));
        idGenerator.setId(2L);
        categoryRepository.save(Category.ofCreated(idGenerator, "교통"));
        idGenerator.setId(3L);
        categoryRepository.save(Category.ofCreated(idGenerator, "문화"));

        // when
        final var categories = categoryService.findCategories();

        // then
        assertThat(categories.get(0).getId()).isEqualTo(1L);
        assertThat(categories.get(0).getName()).isEqualTo("식비");
        assertThat(categories.get(1).getId()).isEqualTo(2L);
        assertThat(categories.get(1).getName()).isEqualTo("교통");
        assertThat(categories.get(2).getId()).isEqualTo(3L);
        assertThat(categories.get(2).getName()).isEqualTo("문화");
    }
}