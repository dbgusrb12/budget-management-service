package com.hg.budget.domain.category.port;

import com.hg.budget.domain.category.Category;
import com.hg.budget.domain.persistence.category.CategoryEntity;
import com.hg.budget.domain.persistence.category.CategoryEntityRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DefaultCategoryRepository implements CategoryRepository {

    private final CategoryEntityRepository categoryEntityRepository;

    @Override
    public void save(Category category) {
        categoryEntityRepository.save(toEntity(category));
    }

    @Override
    public Category findById(Long id) {
        return categoryEntityRepository.findById(id)
            .map(this::fromEntity)
            .orElse(Category.ofNotExist());
    }

    @Override
    public Category findByName(String name) {
        final CategoryEntity category = categoryEntityRepository.findByName(name);
        if (category == null) {
            return Category.ofNotExist();
        }
        return fromEntity(category);
    }

    @Override
    public List<Category> findAll() {
        return categoryEntityRepository.findAll().stream()
            .map(this::fromEntity)
            .collect(Collectors.toList());
    }

    private Category fromEntity(CategoryEntity category) {
        return Category.of(category.getId(), category.getName());
    }

    private CategoryEntity toEntity(Category category) {
        return CategoryEntity.of(category.getId(), category.getName());
    }
}
