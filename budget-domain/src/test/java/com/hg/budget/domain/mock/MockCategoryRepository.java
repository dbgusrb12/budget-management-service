package com.hg.budget.domain.mock;

import com.hg.budget.domain.category.Category;
import com.hg.budget.domain.category.port.CategoryRepository;
import java.util.ArrayList;
import java.util.List;

public class MockCategoryRepository implements CategoryRepository {

    private final List<Category> categories = new ArrayList<>();

    @Override
    public void save(Category category) {
        categories.add(category);
    }

    @Override
    public Category findById(Long id) {
        return categories.stream()
            .filter(category -> category.getId().equals(id))
            .findFirst()
            .orElse(Category.ofNotExist());
    }

    @Override
    public Category findByName(String name) {
        return categories.stream()
            .filter(category -> category.getName().equals(name))
            .findFirst()
            .orElse(Category.ofNotExist());
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(categories);
    }
}
